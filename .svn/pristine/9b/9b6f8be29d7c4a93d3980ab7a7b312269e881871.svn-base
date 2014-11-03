package com.b509.xiaoliao.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.os.Environment;
import android.widget.ImageView;


public class ImageUtil
{
    /**
     * 头像的圆角
     */
    private static final int HEAD_ROTE = 8;
    
    private static final String TAG = "ImageUtil";
    
    /**
     * 构造图片类
     */
    public ImageUtil()
    {
        
    }
    
    /**
     * 
     * 将图片转化给byte[]操作
     * 
     * @param bm 图片对象
     * @return 图片byte[]
     */
    public static byte[] bitmap2Bytes(Bitmap bm)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        
        return baos.toByteArray();
    }
    
    /**
     * [释放Bitmap内存]<BR>
     * [功能详细描述]
     * @param bitmap Bitmap 
     */
    public static void recycleIfNeeded(Bitmap bitmap)
    {
        if (bitmap != null && !bitmap.isRecycled())
        {
            bitmap.recycle();
        }
    }
    
    /**
     * 
     * 将byte[]转化成图片
     * 
     * @param b 图片的byte[]
     * @return 图片对象
     */
    public static Bitmap bytes2Bimap(byte[] b)
    {
        if (b.length != 0)
        {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        }
        else
        {
            return null;
        }
    }
    
    /**
     * 
     * 将Drawable转换为Bitmap
     * 
     * @param drawable Drawable对象
     * @return bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable)
    {
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,
                0,
                drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        
        return bitmap;
    }
    
    /**
     *图片旋转
     * @param bmpOrg 原图片
     * @param rotate 旋转角度（0~360度）
     * @return Bitmap 旋转后的图片
     */
    public static Bitmap rotateBitmap(Bitmap bmpOrg, int rotate)
    {
        int width = bmpOrg.getWidth();
        int height = bmpOrg.getHeight();
        Matrix matrix = new Matrix();
        matrix.postRotate(rotate);
        
        Bitmap resizeBitmap = Bitmap.createBitmap(bmpOrg,
                0,
                0,
                width,
                height,
                matrix,
                true);
        
        if (null != bmpOrg && !bmpOrg.isRecycled())
        {
            bmpOrg.recycle();
            bmpOrg = null;
        }
        
        return resizeBitmap;
    }
    
    /**
     * 图片缩放
     * @param bmp 对图片进行缩放
     * @param scaleWidth 缩放宽度， 0~1为缩小，大于1为放大
     * @param scaleHeight 缩放高度， 0~1为缩小，大于1为放大
     * @return 缩放后的图
     */
    public Bitmap changeBitmap(Bitmap bmp, double scaleWidth, double scaleHeight)
    {
        int bmpWidth = bmp.getWidth();
        int bmpHeight = bmp.getHeight();
        float fScaleWidth = (float) scaleWidth;
        float fScaleHeight = (float) scaleHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(fScaleWidth, fScaleHeight);
        Bitmap resizeBmp = Bitmap.createBitmap(bmp,
                0,
                0,
                bmpWidth,
                bmpHeight,
                matrix,
                true);
        
        return resizeBmp;
    }
    
    /**
     * 
     * 通过byte数组去给指定的ImageView设置圆角背景
     * 
     * @param data 图片数据
     * @param width 图片宽度
     * @param height 图片高度
     * @param adii 圆角大小
     * @param imageView ImageView对象
     */
    public static void drawRoundCorner(byte[] data, int width, int height,
            int adii, ImageView imageView)
    {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        if (bitmap != null)
        {
            drawRoundCorner(bitmap, width, height, adii, imageView);
        }
    }
    
    /**
     * 
     * 用于给指定头像生成圆角的头像
     * 
     * @param bitmap 头像图片
     * @param width 图片宽度
     * @param height 图片长度
     * @param adii 圆角大小
     * @param imageView ImageView对象
     */
    public static void drawRoundCorner(Bitmap bitmap, int width, int height,
            int adii, ImageView imageView)
    {
        Drawable dwbRound = drawRoundCornerForDrawable(bitmap,
                width,
                height,
                adii);
        imageView.setImageDrawable(dwbRound);
    }
    
    /**
     * 
     * 用于给指定头像生成圆角的头像
     * 
     * @param bitmap 头像图片
     * @param width 图片宽度
     * @param height 图片长度
     * @param adii 圆角大小
     * @return Drawable类型的圆脚头像
     */
    public static Drawable drawRoundCornerForDrawable(Bitmap bitmap, int width,
            int height, int adii)
    {
        Shape shpRound = new RoundRectShape(new float[] { adii, adii, adii,
                adii, adii, adii, adii, adii }, null, null);
        ShapeDrawable dwbRound = new ShapeDrawable(shpRound);
        dwbRound.setIntrinsicWidth(width);
        dwbRound.setIntrinsicHeight(height);
        Shader shdBitmap = new BitmapShader(bitmap, Shader.TileMode.MIRROR,
                Shader.TileMode.MIRROR);
        Matrix matrix = new Matrix();
        matrix.setScale((float) width / bitmap.getWidth(), (float) height
                / bitmap.getHeight());
        shdBitmap.setLocalMatrix(matrix);
        dwbRound.getPaint().setShader(shdBitmap);
        dwbRound.getPaint().setFlags(dwbRound.getPaint().getFlags()
                | Paint.ANTI_ALIAS_FLAG);
        
        return dwbRound;
    }
    
    /**
     * 
     * 根据传进来的聊吧成员头像生成聊吧头像<BR>
     * [功能详细描述]
     * 
     * @param backgroundId 背景图片
     * @param drawables 聊吧头像
     * @param width   头像宽度
     * @param height  头像高度
     * @param context context对象
     * @return Bitmap 生成的聊吧头像
     */
    public static Bitmap createChatBarBitmap(int backgroundId,
            List<Drawable> drawables, int width, int height, Context context)
    {
        Drawable background = context.getResources().getDrawable(backgroundId);
        // 首先需要生成指定大小的背景，画到canvas上去
        Bitmap bitmap = Bitmap.createBitmap(width,
                height,
                background.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                        : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        background.setBounds(0, 0, width, height);
        background.draw(canvas);
        if (drawables != null && drawables.size() > 0)
        {
            Rect[][] rects = createRects(width, height);
            // 然后根据list的大小进行不同画法
            Drawable drawable = null;
            int size = drawables.size();
            if (size > 4)
            {
                size = 4;
            }
            switch (size)
            {
                case 1:
                    drawable = drawables.get(0);
                    drawable.setBounds(rects[0][0]);
                    drawable.draw(canvas);
                    break;
                case 2:
                    drawable = drawables.get(0);
                    drawable.setBounds(rects[0][0]);
                    drawable.draw(canvas);
                    drawable = drawables.get(1);
                    drawable.setBounds(rects[0][1]);
                    drawable.draw(canvas);
                    break;
                case 3:
                    drawable = drawables.get(0);
                    drawable.setBounds(rects[0][0]);
                    drawable.draw(canvas);
                    drawable = drawables.get(1);
                    drawable.setBounds(rects[0][1]);
                    drawable.draw(canvas);
                    drawable = drawables.get(2);
                    drawable.setBounds(rects[1][0]);
                    drawable.draw(canvas);
                    break;
                case 4:
                    drawable = drawables.get(0);
                    drawable.setBounds(rects[0][0]);
                    drawable.draw(canvas);
                    drawable = drawables.get(1);
                    drawable.setBounds(rects[0][1]);
                    drawable.draw(canvas);
                    drawable = drawables.get(2);
                    drawable.setBounds(rects[1][0]);
                    drawable.draw(canvas);
                    drawable = drawables.get(3);
                    drawable.setBounds(rects[1][1]);
                    drawable.draw(canvas);
                    break;
                default:
                    break;
            }
        }
        return bitmap;
    }
    

    
    /**
     * 
     * 根据指定的高度和宽度生成对应的坐标<BR>
     * [功能详细描述]
     * @param width
     * @param height
     * @return
     */
    private static Rect[][] createRects(int width, int height)
    {
        Rect[][] rects = new Rect[2][2];
        int blank = 2;
        // 先算出坐标
        int imageWidth = (width - blank * 3) / 2;
        int imageHeight = (height - blank * 3) / 2;
        Rect rect = null;
        for (int i = 0; i < 2; i++)
        {
            for (int j = 0; j < 2; j++)
            {
                rect = new Rect();
                rect.left = imageWidth * j + blank * (j + 1);
                rect.top = imageHeight * i + blank * (i + 1);
                rect.right = imageWidth * (j + 1) + blank * (j + 1);
                rect.bottom = imageHeight * (i + 1) + blank * (i + 1);
                rects[i][j] = rect;
            }
        }
        return rects;
    }
    
    /**
     * 根据最小边长进行压缩图片，以便向服务器上传
     * 
     * @param path 图片路径
     * @return 压缩后的位图
     */
    public static Bitmap getFitBitmap(String path)
    {
        if (path == null)
        {
            return null;
        }
        try
        {
            //图片最大宽度/高度
            int imageWidth = 800;
            
            int imageHeight = 480;
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(path, opts);
            
            int srcWidth = opts.outWidth;
            int srcHeight = opts.outHeight;
            
            int destWidth = 0;
            int destHeight = 0;
            // 缩放的比例
            double ratio = 0.0;
            // if (srcWidth * srcHeight < (IMAGE_WIDTH * IMAGE_HEIGHT))
            // {
            // return BitmapFactory.decodeFile(path);
            // }
            if (srcWidth < srcHeight)
            {
                ratio = (double) srcWidth / imageWidth;
                if (ratio > 1.0)
                {
                    destHeight = (int) (srcHeight / ratio);
                    destWidth = imageWidth;
                }
                else
                {
                    return BitmapFactory.decodeFile(path);
                }
                
            }
            else
            {
                ratio = (double) srcHeight / imageHeight;
                if (ratio > 1.0)
                {
                    destWidth = (int) (srcWidth / ratio);
                    destHeight = imageHeight;
                }
                else
                {
                    return BitmapFactory.decodeFile(path);
                }
                
            }
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            double x = Math.log(ratio) / Math.log(2);
            int k = (int) Math.ceil(x);
            int j = (int) Math.pow(2, k);
            newOpts.inSampleSize = j;
            newOpts.inJustDecodeBounds = false;
            newOpts.outHeight = destHeight;
            newOpts.outWidth = destWidth;
            
            // Tell to gc that whether it needs free memory, the Bitmap can
            // be cleared
            newOpts.inPurgeable = true;
            // Which kind of reference will be used to recover the Bitmap
            // data after being clear, when it will be used in the future
            newOpts.inInputShareable = true;
            // Allocate some temporal memory for decoding
            newOpts.inTempStorage = new byte[64 * 1024];
            
            Bitmap destBm = BitmapFactory.decodeFile(path, newOpts);
            
            return destBm;
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 生成小图文件并获取该文件地址(与getFitBitmap(String)配合使用)<BR>
     * @param bitmap Bitmap
     * @return 生成压缩后的小图地址
     */
    public static String saveBitmap(Bitmap bitmap,String savePath)
    {

        if (null == savePath)
        {
            return null;
        }
        String smallImgPath = savePath
                +"/"+ String.valueOf(System.currentTimeMillis()) + ".jpg";
        
        File file = new File(smallImgPath);
        BufferedOutputStream bos;
        try
        {
            bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        }
        catch (FileNotFoundException e)
        {
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return smallImgPath;
    }
}
