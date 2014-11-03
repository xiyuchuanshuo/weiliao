package com.b509.xiaoliao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Vibrator;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.b509.xiaoliao.inter.IconReceived;
import com.b509.xiaoliao.listener.Listener;
import com.b509.xiaoliao.listener.TCPFileListener;
import com.b509.xiaoliao.model.UDPMessage;
import com.b509.xiaoliao.model.User;
import com.b509.xiaoliao.service.ChatService;
import com.b509.xiaoliao.service.ChatService.MyBinder;
import com.b509.xiaoliao.tools.Medias;
import com.b509.xiaoliao.tools.Shake;
import com.b509.xiaoliao.tools.Tools;
import com.b509.xiaoliao.util.Constant;
import com.b509.xiaoliao.util.LocalMemoryCache;
import com.b509.xiaoliao.util.Util;
import com.b509.xiaoliao.view.COSlidingMenu;
import com.b509.xiaoliao.view.COSlidingState;
import com.b509.xiaoliao.view.LiftListener;
import com.b509.xiaoliao.view.RightListener;
import com.b509.xiaoliao.widget.PullToRefreshExpandableListView;
import com.b509.xiaoliao.widget.PullToRefreshExpandableListView.OnRefreshListener;

public class MainActivity extends mActivity implements IconReceived {
	public static final String TAG = "MainActivity";
	private COSlidingMenu coSlidingMenu;
	private PullToRefreshExpandableListView listView;

	private List<User> users = new ArrayList<User>();
	private Map<String, Queue<UDPMessage>> messages;
	MyServiceConnection connection;

	public static MyBinder binder;
	private boolean binded;
	private MyAdapter adapter;
	private UserBroadcastReceiver receiver = new UserBroadcastReceiver();
	private Map<String, Message> iconMap = new HashMap<String, Message>();

	public static final String ACTION_ADD_USER = "com.b509.xiaoliao.adduser";

	private TCPFileListener fileListener;

	private static ImageButton touxiang;
	private static String iconPath;// 头像保存路径
	private final int CUT_PHOTO_REQUEST_CODE = 201;
	private final int SELECT_PHOTO_REQUEST_CODE = 200;
	public static String iconName="me";
	Vibrator v;
	public static final String[] keywords = { "安卓语音阅读器", "安卓语音", "阅读器", "语音阅读器",
		"nil",//
		"逆网狂流", "语音阅读器", "青苹果", "青苹果免费小说", "小说下载",//
		"作者", "fbreader", "FBReader"};
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			try {
				// 刷新头像
				((ImageView) msg.obj).setImageBitmap(Util
						.getRoundedCornerBitmap(LocalMemoryCache.getInstance()
								.get(msg.getData().getString("key"))));
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		coSlidingMenu = (COSlidingMenu) findViewById(R.id.slidingMenu);
		ViewGroup leftView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.left_v, null);
		ViewGroup rightView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.right_v, null);
		ViewGroup centerView = (ViewGroup) getLayoutInflater().inflate(
				R.layout.center_v, null);
		coSlidingMenu.setCenterView(centerView);
		int leftWidth = (int) getResources()
				.getDimension(R.dimen.leftViewWidth);
		int rightWidth = (int) getResources().getDimension(
				R.dimen.rightViewWidth);
		coSlidingMenu.setLeftView(leftView, leftWidth);
		coSlidingMenu.setRightView(rightView, rightWidth);

		View ivRight = centerView.findViewById(R.id.ivRight);
		v = Shake.getV(this);
		Medias.initSound(this);
		ivRight.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "点击了右边设置按钮");
				if (coSlidingMenu.getCurrentUIState() == COSlidingState.SHOWRIGHT) {
					coSlidingMenu.showViewState(COSlidingState.SHOWCENTER);
				} else {
					coSlidingMenu.showViewState(COSlidingState.SHOWRIGHT);
				}
			}
		});
		View ivLeft = centerView.findViewById(R.id.ivLeft);

		ivLeft.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Log.d(TAG, "点击了左边设置按钮");
				if (coSlidingMenu.getCurrentUIState() == COSlidingState.SHOWLEFT) {
					coSlidingMenu.showViewState(COSlidingState.SHOWCENTER);
				} else {
					coSlidingMenu.showViewState(COSlidingState.SHOWLEFT);
				}
			}
		});

		iconPath = getFilesDir() + File.separator + iconName;
		touxiang = (ImageButton) leftView.findViewById(R.id.touxiang);

		// 设置头像
		Bitmap bitmap = LocalMemoryCache.getInstance().get(iconName);
		if (bitmap == null) {
			bitmap = BitmapFactory.decodeFile(iconPath);
			if (bitmap != null) {
				touxiang.setImageBitmap(Util.getRoundedCornerBitmap(bitmap));
				LocalMemoryCache.getInstance().put(iconName, bitmap);
			} else {
				touxiang.setImageResource(R.drawable.ic_launcher);
			}
		} else {
			touxiang.setImageBitmap(Util.getRoundedCornerBitmap(bitmap));
		}

		touxiang.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					Intent i = new Intent(Intent.ACTION_PICK,
							Media.EXTERNAL_CONTENT_URI);
					i.setType("image/*");
					startActivityForResult(i, SELECT_PHOTO_REQUEST_CODE);
				} catch (Exception e) {
					Toast.makeText(MainActivity.this, "抱歉，您的手机不支持头像设置",
							Toast.LENGTH_SHORT).show();
				}

			}
		});
		init(centerView);
		fileListener = TCPFileListener.getInstance();
		if (!fileListener.isRunning()) {
			try {
				fileListener.open();
				fileListener.setIconReceived(this);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		// 设置左边的listview
		LiftListener.LiftListeners(MainActivity.this, leftView);

		// 设置右边的listview
		RightListener.RightListeners(MainActivity.this, rightView);
	}

	@Override
	protected void onStart() {
		super.onStart();

		listView.setAdapter(adapter = new MyAdapter());
		adapter.notifyDataSetChanged();
		if (fileListener != null)
			fileListener.setOnProgressUpdate(null);
	}

	/**
	 * 做一些初始化的动作
	 */
	private void init(ViewGroup centerView) {
		Intent intent = new Intent(MainActivity.this, ChatService.class);
		startService(intent);
		bindService(intent, connection = new MyServiceConnection(),
				Context.BIND_AUTO_CREATE);
		IntentFilter filter = new IntentFilter(ACTION_ADD_USER);
		registerReceiver(receiver, filter);

		listView = (PullToRefreshExpandableListView) centerView
				.findViewById(R.id.main_listview);
		listView.setGroupIndicator(getResources().getDrawable(
				R.drawable.listview_open_selector));
		listView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				if (binded) {
					unbindService(connection);
					binded = false;
				}
				Intent intent = new Intent(MainActivity.this, MessageChat.class);
				switch (groupPosition) {
				case 0:
					String ip = MyApplication.mainInstance.getLocalIp();
					if (ip == null) {
						showToast("请检测wifi");
						return false;
					}
					intent.putExtra("IP", ip);
					intent.putExtra("DeviceCode",
							MyApplication.mainInstance.getDeviceCode());
					intent.putExtra("name",
							MyApplication.mainInstance.getMyName());
					break;
				case 1:
					User user = users.get(childPosition);
					intent.putExtra("IP", user.getIp());
					intent.putExtra("DeviceCode", user.getDeviceCode());
					intent.putExtra("name", user.getUserName());
					break;
				case 2:
					intent = new Intent(MainActivity.this, RoomChat.class);
				}
				startActivity(intent);// 跳转到个人聊天界面
				return false;
			}
		});
		listView.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				new AsyncTask<Void, Void, Void>() {
					protected Void doInBackground(Void... params) {
						try {
							if (binder != null)
								binder.noticeOnline();
							Thread.sleep(300);
						} catch (Exception e) {
							e.printStackTrace();
						}
						return null;
					}

					@Override
					protected void onPostExecute(Void result) {
						listView.onRefreshComplete();
					}

				}.execute();
			}
		});
		// initad();
	}

	// /**
	// * 初始化广告
	// */
	// private void initad(){
	// AdManager.getInstance(this).init(Constant.id,Constant.key, false);
	// LinearLayout adLayout = (LinearLayout) findViewById(R.id.adLayout);
	// AdView adView = new AdView(this, AdSize.SIZE_320x50);
	// adLayout.addView(adView);
	// }

	long oldTime;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			long currentTime = System.currentTimeMillis();
			if (currentTime - oldTime < 3 * 1000) {
				finish();
			} else {
				showToast("再按一次退出");
				if (coSlidingMenu.getCurrentUIState() == COSlidingState.SHOWLEFT) {
					coSlidingMenu.showViewState(COSlidingState.SHOWCENTER);
				} else {
					coSlidingMenu.showViewState(COSlidingState.SHOWLEFT);
				}
				oldTime = currentTime;
			}
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (binded)
			unbindService(connection);
		stopService(new Intent(MainActivity.this, ChatService.class));
		unregisterReceiver(receiver);
		if (fileListener != null)
			try {
				fileListener.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}

	/**
	 * 用来通知刷新列表
	 */
	class UserBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (binder != null) {
				users.clear();
				Set<Entry<String, User>> set = binder.getUsers().entrySet();
				for (Entry<String, User> entry : set)
					users.add(entry.getValue());
				if (adapter == null) {
					adapter = new MyAdapter();
					listView.setAdapter(adapter);
				}
				adapter.notifyDataSetChanged();
			} else {
				unbindService(connection);
				binded = false;
				bindService(new Intent(MainActivity.this, ChatService.class),
						connection = new MyServiceConnection(),
						Context.BIND_AUTO_CREATE);
			}
		}

	}

	public class MyServiceConnection implements ServiceConnection {
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			binder = (MyBinder) service;
			messages = binder.getMessages();
			binded = true;
		}

		@Override
		public void onServiceDisconnected(ComponentName name) {
		}

	}

	class MyAdapter extends BaseExpandableListAdapter {

		String[] group = { "自己", "在线", "聊天室" };

		@Override
		public Object getChild(int arg0, int arg1) {
			return null;
		}

		@Override
		public long getChildId(int groupPosition, int childPosition) {
			return 0;
		}

		@Override
		public View getChildView(int groupPosition, int childPosition,
				boolean isLastChild, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.main_listview_child_item, null);
				holder.userName = (TextView) convertView
						.findViewById(R.id.main_listview_child_item_name);
				holder.ip = (TextView) convertView
						.findViewById(R.id.main_listview_child_item_ip);
				holder.msgNum = (TextView) convertView
						.findViewById(R.id.main_listview_child_item_msg_num);
				holder.icon = (ImageView) convertView
						.findViewById(R.id.main_listview_child_item_icon);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			switch (groupPosition) {
			case 0:// 自己
				String name = MyApplication.mainInstance.getMyName();
				System.out.println("name======" + name);
				holder.userName.setText(name);
				holder.ip.setText(MyApplication.mainInstance.getLocalIp());
				holder.msgNum.setVisibility(View.INVISIBLE);
				Bitmap bitmap = LocalMemoryCache.getInstance().get(
						com.b509.xiaoliao.MainActivity.iconName);
				if (bitmap == null) {
					bitmap = BitmapFactory
							.decodeFile(MyApplication.iconPath
									+ com.b509.xiaoliao.MainActivity.iconName);
					if (bitmap != null) {
						holder.icon.setImageBitmap(Util
								.getRoundedCornerBitmap(bitmap));
						LocalMemoryCache.getInstance().put(
								com.b509.xiaoliao.MainActivity.iconName, bitmap);
					} else {
						holder.icon.setImageResource(R.drawable.ic_launcher);
					}
				} else {
					holder.icon.setImageBitmap(Util
							.getRoundedCornerBitmap(bitmap));
				}

				break;
			case 1:// 在线
				User user = users.get(childPosition);
				holder.userName.setText(user.getUserName());
				holder.ip.setText(user.getIp());
				Queue<UDPMessage> msgs = messages.get(user.getIp());
				if (msgs != null && msgs.size() > 0) {
					holder.msgNum.setVisibility(View.VISIBLE);
					holder.msgNum.setText(msgs.size() + "");
					v.vibrate(new long[] { 100, 10, 100, 100 }, -1);
					Medias.play(0);
					
				} else {
					holder.msgNum.setVisibility(View.INVISIBLE);
				}
				Log.i("zky", "user.getDeviceCode()======="+user.getDeviceCode());
				Bitmap bitmap1 = LocalMemoryCache.getInstance().get(
						user.getDeviceCode());// 用设备id来标识唯一头像
				if (bitmap1 == null) {// 内存中没有
					bitmap1 = BitmapFactory
							.decodeFile(MyApplication.iconPath
									+ user.getDeviceCode());// 从硬盘上获取
					if (bitmap1 != null) {
						holder.icon.setImageBitmap(Util
								.getRoundedCornerBitmap(bitmap1));
						LocalMemoryCache.getInstance().put(
								user.getDeviceCode(), bitmap1);// 放进缓存
						if (!user.isRefreshIcon()) {// 第一次展示则再次请求刷新
							reFreashIcon(user, holder.icon);
						}
					} else {// 磁盘也没有，则发送消息获取
						holder.icon.setImageResource(R.drawable.ic_launcher);
						reFreashIcon(user, holder.icon);
					}
				} else {
					holder.icon.setImageBitmap(Util
							.getRoundedCornerBitmap(bitmap1));
					if (!user.isRefreshIcon()) {// 第一次展示则再次请求刷新
						reFreashIcon(user, holder.icon);
					}
				}
				break;
			case 2:// 聊天室
				Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),
						R.drawable.all_people_icon);
				holder.icon
						.setImageBitmap(Util.getRoundedCornerBitmap(bitmap2));
				holder.userName.setText("所有");
				holder.ip.setText("接收所有在线人消息");
				msgs = messages.get(Constant.ALL_ADDRESS);
				if (msgs != null && msgs.size() > 0) {
					holder.msgNum.setVisibility(View.VISIBLE);
					holder.msgNum.setText(msgs.size() + "");
				} else {
					holder.msgNum.setVisibility(View.INVISIBLE);
				}
				break;
			}
			return convertView;
		}

		/**
		 * 请求图片
		 * 
		 * @param user
		 * @param view
		 */
		private void reFreashIcon(User user, ImageView view) {
			if (binder != null)
				try {
					UDPMessage message = MyApplication.mainInstance
							.getMyUdpMessage("", Listener.REQUIRE_ICON);
					binder.sendMsg(message, InetAddress.getByName(user.getIp()));
					Message msg = handler.obtainMessage();
					msg.obj = view;
					iconMap.put(user.getDeviceCode(), msg);// 记录当前位置的ImageView对象
					user.setRefreshIcon(true);
				} catch (UnknownHostException e) {
					e.printStackTrace();
				}
		}

		@Override
		public int getChildrenCount(int groupPosition) {
			switch (groupPosition) {
			case 0:
				return 1;
			case 1:
				return users.size();
			case 2:
				return 1;
			}
			return 0;
		}

		@Override
		public Object getGroup(int groupPosition) {
			return null;
		}

		@Override
		public int getGroupCount() {
			return group.length;
		}

		@Override
		public long getGroupId(int groupPosition) {
			return 0;
		}

		@Override
		public View getGroupView(int groupPosition, boolean isExpanded,
				View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = getLayoutInflater().inflate(
						R.layout.main_listview_group_item, null);
				holder.userName = (TextView) convertView.findViewById(R.id.txt);
				holder.ip = (TextView) convertView.findViewById(R.id.num);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.userName.setText(group[groupPosition]);
			if (groupPosition == 0) {
				holder.ip.setText("[1]");
			} else if (groupPosition == 1) {
				holder.ip.setText("[" + users.size() + "]");
			} else if (groupPosition == 2) {
				holder.ip.setText("[1]");
			}
			return convertView;
		}

		@Override
		public boolean hasStableIds() {
			return false;
		}

		@Override
		public boolean isChildSelectable(int groupPosition, int childPosition) {
			return true;
		}
	}

	class ViewHolder {
		TextView userName;
		TextView ip;
		TextView msgNum;
		ImageView icon;
	}

	@Override
	public void iconReceived(String fileName) {
		Message msg = iconMap.get(fileName);
		if (msg != null) {
			Bitmap bitmap = BitmapFactory
					.decodeFile(MyApplication.iconPath + fileName);
			if (bitmap != null) {
				LocalMemoryCache.getInstance().put(fileName, bitmap);
				Bundle bundle = new Bundle();
				bundle.putString("key", fileName);
				msg.setData(bundle);
				handler.sendMessage(msg);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == SELECT_PHOTO_REQUEST_CODE && resultCode == RESULT_OK
				&& data != null) {
			Uri uri = data.getData();
			if (uri != null) {
				final Intent intent = new Intent(
						"com.android.camera.action.CROP");
				intent.setDataAndType(uri, "image/*");
				intent.putExtra("crop", "true");
				intent.putExtra("aspectX", 1);
				intent.putExtra("aspectY", 1);
				intent.putExtra("outputX", 100);
				intent.putExtra("outputY", 100);
				intent.putExtra("return-data", true);
				startActivityForResult(intent, CUT_PHOTO_REQUEST_CODE);
			}
		} else if (requestCode == CUT_PHOTO_REQUEST_CODE
				&& resultCode == RESULT_OK && data != null) {
			try {
				Bitmap bitmap = data.getParcelableExtra("data");
				bitmap = Util.getRoundedCornerBitmap(bitmap);
				touxiang.setImageBitmap(bitmap);
				File file = new File(iconPath);
				file.delete();
				file.createNewFile();
				FileOutputStream outputStream = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
				outputStream.flush();
				outputStream.close();
				LocalMemoryCache.getInstance().put(iconName, bitmap);
				showToast("头像保存成功");
			} catch (IOException e) {
				e.printStackTrace();
				showToast("头像保存失败");
			}
		}
	}
}