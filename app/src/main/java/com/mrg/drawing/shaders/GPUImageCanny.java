package com.mrg.drawing.shaders;

import jp.co.cyberagent.android.gpuimage.GPUImage3x3TextureSamplingFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageDirectionalSobelEdgeDetectionFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageFilterGroup;
import jp.co.cyberagent.android.gpuimage.GPUImageGaussianBlurFilter;
import jp.co.cyberagent.android.gpuimage.GPUImageGrayscaleFilter;

/**
 * Created by Mrugi on 2016-03-24.
 */
public class GPUImageCanny extends GPUImageFilterGroup {
    public static final String MY_NMS_FRAGMENT_SHADER = "" +
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "varying highp vec2 textureCoordinate;\n" +
            "varying highp vec2 leftTextureCoordinate;\n" +
            "varying highp vec2 rightTextureCoordinate;\n" +
            "\n" +
            "varying highp vec2 topTextureCoordinate;\n" +
            "varying highp vec2 topLeftTextureCoordinate;\n" +
            "varying highp vec2 topRightTextureCoordinate;\n" +
            "\n" +
            "varying highp vec2 bottomTextureCoordinate;\n" +
            "varying highp vec2 bottomLeftTextureCoordinate;\n" +
            "varying highp vec2 bottomRightTextureCoordinate;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "lowp float bottomColor = texture2D(inputImageTexture, bottomTextureCoordinate).r;\n" +
            "lowp float bottomLeftColor = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\n" +
            "lowp float bottomRightColor = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\n" +
            "lowp vec4 centerColor = texture2D(inputImageTexture, textureCoordinate);\n" +
            "lowp float leftColor = texture2D(inputImageTexture, leftTextureCoordinate).r;\n" +
            "lowp float rightColor = texture2D(inputImageTexture, rightTextureCoordinate).r;\n" +
            "lowp float topColor = texture2D(inputImageTexture, topTextureCoordinate).r;\n" +
            "lowp float topRightColor = texture2D(inputImageTexture, topRightTextureCoordinate).r;\n" +
            "lowp float topLeftColor = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\n" +
            "\n" +
            "// Use a tiebreaker for pixels to the left and immediately above this one\n" +
            "lowp float multiplier = 1.0 - step(centerColor.r, topColor);\n" +
            "multiplier = multiplier * 1.0 - step(centerColor.r, topLeftColor);\n" +
            "multiplier = multiplier * 1.0 - step(centerColor.r, leftColor);\n" +
            "multiplier = multiplier * 1.0 - step(centerColor.r, bottomLeftColor);\n" +
            "\n" +
            "lowp float maxValue = max(centerColor.r, bottomColor);\n" +
            "maxValue = max(maxValue, bottomRightColor);\n" +
            "maxValue = max(maxValue, rightColor);\n" +
            "maxValue = max(maxValue, topRightColor);\n" +
            "\n" +
            "if(maxValue==centerColor.r && centerColor.r>0.2)"+
            "gl_FragColor = vec4(1.0);\n" +
            "else\n"+
            "gl_FragColor = vec4(0.0);\n" +
            "float theta;"+
            "if(centerColor.y != 0.0) "+
            "theta = atan(centerColor.z/centerColor.y)/3.14; "+
            "else "+
            "theta=1.0; "+
            "bool max=false; "+
            "if(theta <= -0.75 || theta > 0.75)"+
            "{ "+
            "if(topColor<=centerColor.r && bottomColor<=centerColor.r)"+
            "max=true; "+
            "}"+
            "else if(theta >= -0.75 && theta < -0.25)"+
            "{ "+
            "if(topRightColor<=centerColor.r && bottomLeftColor<=centerColor.r)"+
            "max=true;"+
            "}"+
            "else if(theta >= -0.25 && theta < 0.25)"+
            "{ "+
            "if(leftColor<=centerColor.r && rightColor<=centerColor.r)"+
            "max=true;"+
            "}"+
            "else if(theta >= 0.25 && theta < 0.75)"+
            "{ "+
            "if(topLeftColor<=centerColor.r && bottomRightColor<=centerColor.r)"+
            "max=true;"+
            "}\n"+
            ""+
            "if(max && centerColor.r>0.10) "+
            "gl_FragColor = vec4(1.0);\n" +
            "else \n"+
            "gl_FragColor = vec4(0.0);\n" +
            "}\n";
    public GPUImageCanny() {
        super();
        addFilter(new GPUImageGrayscaleFilter());
        addFilter(new GPUImageGaussianBlurFilter(1f));
        addFilter(new GPUImageDirectionalSobelEdgeDetectionFilter());
        addFilter(new GPUImage3x3TextureSamplingFilter(MY_NMS_FRAGMENT_SHADER));
    }

    public void setLineSize(final float size) {
        ((GPUImage3x3TextureSamplingFilter) getFilters().get(2)).setLineSize(0.8f);
    }
}
