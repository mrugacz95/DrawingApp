package com.mrg.drawing.shaders;


import jp.co.cyberagent.android.gpuimage.GPUImageTwoPassTextureSamplingFilter;

public class GPUImageCannyEdgeDet extends GPUImageTwoPassTextureSamplingFilter {
        public static final String THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER = "" +
                "attribute vec4 position;\n" +
                "attribute vec4 inputTextureCoordinate;\n" +
                "\n" +
                "uniform highp float texelWidth; \n" +
                "uniform highp float texelHeight; \n" +
                "\n" +
                "varying vec2 textureCoordinate;\n" +
                "varying vec2 leftTextureCoordinate;\n" +
                "varying vec2 rightTextureCoordinate;\n" +
                "\n" +
                "varying vec2 topTextureCoordinate;\n" +
                "varying vec2 topLeftTextureCoordinate;\n" +
                "varying vec2 topRightTextureCoordinate;\n" +
                "\n" +
                "varying vec2 bottomTextureCoordinate;\n" +
                "varying vec2 bottomLeftTextureCoordinate;\n" +
                "varying vec2 bottomRightTextureCoordinate;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    gl_Position = position;\n" +
                "\n" +
                "    vec2 widthStep = vec2(texelWidth, 0.0);\n" +
                "    vec2 heightStep = vec2(0.0, texelHeight);\n" +
                "    vec2 widthHeightStep = vec2(texelWidth, texelHeight);\n" +
                "    vec2 widthNegativeHeightStep = vec2(texelWidth, -texelHeight);\n" +
                "\n" +
                "    textureCoordinate = inputTextureCoordinate.xy;\n" +
                "    leftTextureCoordinate = inputTextureCoordinate.xy - widthStep;\n" +
                "    rightTextureCoordinate = inputTextureCoordinate.xy + widthStep;\n" +
                "\n" +
                "    topTextureCoordinate = inputTextureCoordinate.xy - heightStep;\n" +
                "    topLeftTextureCoordinate = inputTextureCoordinate.xy - widthHeightStep;\n" +
                "    topRightTextureCoordinate = inputTextureCoordinate.xy + widthNegativeHeightStep;\n" +
                "\n" +
                "    bottomTextureCoordinate = inputTextureCoordinate.xy + heightStep;\n" +
                "    bottomLeftTextureCoordinate = inputTextureCoordinate.xy - widthNegativeHeightStep;\n" +
                "    bottomRightTextureCoordinate = inputTextureCoordinate.xy + widthHeightStep;\n" +
                "}";
    public static final String DIRECTIONAL_SOBEL_EDGE_DETECTION_FRAGMENT_SHADER = "" +
            "precision mediump float;\n" +
            "\n" +
            "varying vec2 textureCoordinate;\n" +
            "varying vec2 leftTextureCoordinate;\n" +
            "varying vec2 rightTextureCoordinate;\n" +
            "\n" +
            "varying vec2 topTextureCoordinate;\n" +
            "varying vec2 topLeftTextureCoordinate;\n" +
            "varying vec2 topRightTextureCoordinate;\n" +
            "\n" +
            "varying vec2 bottomTextureCoordinate;\n" +
            "varying vec2 bottomLeftTextureCoordinate;\n" +
            "varying vec2 bottomRightTextureCoordinate;\n" +
            "\n" +
            "uniform sampler2D inputImageTexture;\n" +
            "\n" +
            "void main()\n" +
            "{\n" +
            "    float bottomLeftIntensity = (texture2D(inputImageTexture, bottomLeftTextureCoordinate).x+texture2D(inputImageTexture, bottomLeftTextureCoordinate).y+texture2D(inputImageTexture, bottomLeftTextureCoordinate).z)/3.0;\n" +
            "    float topRightIntensity = (texture2D(inputImageTexture, topRightTextureCoordinate).x+texture2D(inputImageTexture, topRightTextureCoordinate).y+texture2D(inputImageTexture, topRightTextureCoordinate).z)/3.0;\n" +
            "    float topLeftIntensity = (texture2D(inputImageTexture, topLeftTextureCoordinate).x+texture2D(inputImageTexture, topLeftTextureCoordinate).y+texture2D(inputImageTexture, topLeftTextureCoordinate).z)/3.0;\n" +
            "    float bottomRightIntensity = (texture2D(inputImageTexture, bottomRightTextureCoordinate).x+texture2D(inputImageTexture, bottomRightTextureCoordinate).y+texture2D(inputImageTexture, bottomRightTextureCoordinate).z)/3.0;\n" +
            "    float leftIntensity = (texture2D(inputImageTexture, leftTextureCoordinate).x+texture2D(inputImageTexture, leftTextureCoordinate).y+texture2D(inputImageTexture, leftTextureCoordinate).z)/3.0;\n" +
            "    float rightIntensity = (texture2D(inputImageTexture, rightTextureCoordinate).x+texture2D(inputImageTexture, rightTextureCoordinate).y+texture2D(inputImageTexture, rightTextureCoordinate).z)/3.0;\n" +
            "    float bottomIntensity = (texture2D(inputImageTexture, bottomTextureCoordinate).x+texture2D(inputImageTexture, bottomTextureCoordinate).y+texture2D(inputImageTexture, bottomTextureCoordinate).z)/3.0;\n" +
            "    float topIntensity = (texture2D(inputImageTexture, topTextureCoordinate).x+texture2D(inputImageTexture, topTextureCoordinate).y+texture2D(inputImageTexture, topTextureCoordinate).z)/3.0;\n" +
            "\n" +
//            "    vec3 xDir = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n" +
//            "    vec3 yDir = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n" +
////            "\n" +
////            "    float gradientMagnitude = length(gradientDirection);\n" +
////            "    vec2 normalizedDirection = normalize(gradientDirection);\n" +
////            "    normalizedDirection = sign(normalizedDirection) * floor(abs(normalizedDirection) + 0.617316); // Offset by 1-sin(pi/8) to set to 0 if near axis, 1 if away\n" +
////            "    normalizedDirection = (normalizedDirection + 1.0) * 0.5; // Place -1.0 - 1.0 within 0 - 1.0\n" +
//            "\n" +
//            "   lowp float val = length(xDir*xDir + yDir*yDir);\n"+
//            "   // gl_FragColor = vec4(gradientMagnitude, normalizedDirection.x, normalizedDirection.y, 1.0);\n" +
//            "lowp float theta;\n"+
//            "if(length(xDir) != 0.0)\n"+
//            "theta =  atan(length(yDir)/length(xDir))/3.14+0.5;\n"+
//            "else\n"+
//            "theta=1.0;\n"+
//            "    gl_FragColor = vec4(vec3(theta), 1.0);\n" +
//            "    //gl_FragColor = topIntensity;\n" +
            "    float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n" +
            "    float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n" +
            "\n" +
            "    float mag = length(vec2(h, v));\n" +
            "\n" +
            "    gl_FragColor = vec4(vec3(mag), 1.0);\n" +
            "gl_FragColor = vec4(1.0 , 0.0, 0.0 ,1.0);"+
            "}";
        public static final String SOBEL_EDGE_DETECTION = "" +
                "precision mediump float;\n" +
                "\n" +
                "varying vec2 textureCoordinate;\n" +
                "varying vec2 leftTextureCoordinate;\n" +
                "varying vec2 rightTextureCoordinate;\n" +
                "\n" +
                "varying vec2 topTextureCoordinate;\n" +
                "varying vec2 topLeftTextureCoordinate;\n" +
                "varying vec2 topRightTextureCoordinate;\n" +
                "\n" +
                "varying vec2 bottomTextureCoordinate;\n" +
                "varying vec2 bottomLeftTextureCoordinate;\n" +
                "varying vec2 bottomRightTextureCoordinate;\n" +
                "\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    float bottomLeftIntensity = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\n" +
                "    float topRightIntensity = texture2D(inputImageTexture, topRightTextureCoordinate).r;\n" +
                "    float topLeftIntensity = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\n" +
                "    float bottomRightIntensity = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\n" +
                "    float leftIntensity = texture2D(inputImageTexture, leftTextureCoordinate).r;\n" +
                "    float rightIntensity = texture2D(inputImageTexture, rightTextureCoordinate).r;\n" +
                "    float bottomIntensity = texture2D(inputImageTexture, bottomTextureCoordinate).r;\n" +
                "    float topIntensity = texture2D(inputImageTexture, topTextureCoordinate).r;\n" +
                "    float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n" +
                "    float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n" +
                "\n" +
                "    float mag = length(vec2(h, v));\n" +
                "\n" +
                "    gl_FragColor = vec4(vec3(mag), 1.0);\n" +
                "}";
        public static final String SOBEL_THRESHOLD_EDGE_DETECTION = "" +
                "\n" +
                "varying vec2 textureCoordinate;\n" +
                "varying vec2 leftTextureCoordinate;\n" +
                "varying vec2 rightTextureCoordinate;\n" +
                "\n" +
                "varying vec2 topTextureCoordinate;\n" +
                "varying vec2 topLeftTextureCoordinate;\n" +
                "varying vec2 topRightTextureCoordinate;\n" +
                "\n" +
                "varying vec2 bottomTextureCoordinate;\n" +
                "varying vec2 bottomLeftTextureCoordinate;\n" +
                "varying vec2 bottomRightTextureCoordinate;\n" +
                "\n" +
                "uniform sampler2D inputImageTexture;\n" +
                "\n" +
                "const highp vec3 W = vec3(0.2125, 0.7154, 0.0721);\n" +
                "\n" +
                "void main()\n" +
                "{\n" +
                "    float bottomLeftIntensity = texture2D(inputImageTexture, bottomLeftTextureCoordinate).r;\n" +
                "    float topRightIntensity = texture2D(inputImageTexture, topRightTextureCoordinate).r;\n" +
                "    float topLeftIntensity = texture2D(inputImageTexture, topLeftTextureCoordinate).r;\n" +
                "    float bottomRightIntensity = texture2D(inputImageTexture, bottomRightTextureCoordinate).r;\n" +
                "    float leftIntensity = texture2D(inputImageTexture, leftTextureCoordinate).r;\n" +
                "    float rightIntensity = texture2D(inputImageTexture, rightTextureCoordinate).r;\n" +
                "    float bottomIntensity = texture2D(inputImageTexture, bottomTextureCoordinate).r;\n" +
                "    float topIntensity = texture2D(inputImageTexture, topTextureCoordinate).r;\n" +
                "    float h = -topLeftIntensity - 2.0 * topIntensity - topRightIntensity + bottomLeftIntensity + 2.0 * bottomIntensity + bottomRightIntensity;\n" +
                "    float v = -bottomLeftIntensity - 2.0 * leftIntensity - topLeftIntensity + bottomRightIntensity + 2.0 * rightIntensity + topRightIntensity;\n" +
                "\n" +
                "    float mag = 1.0 - length(vec2(h, v));\n" +
                "    mag = step(0.9, mag);\n" +
                "\n" +
                //"    gl_FragColor =  vec4(h,v,0.0,1.0); "+
                // "    gl_FragColor = vec4(vec3(sqrt(h*h+v*v)), 1.0);\n" +
                 "    gl_FragColor = vec4(vec3(mag), 1.0);\n" +
                "}\n";

        public static final String NMS_FRAGMENT_SHADER = "" +
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
//                "// Use a tiebreaker for pixels to the left and immediately above this one\n" +
//                "lowp float multiplier = 1.0 - step(centerColor.r, topColor);\n" +
//                "multiplier = multiplier * 1.0 - step(centerColor.r, topLeftColor);\n" +
//                "multiplier = multiplier * 1.0 - step(centerColor.r, leftColor);\n" +
//                "multiplier = multiplier * 1.0 - step(centerColor.r, bottomLeftColor);\n" +
//                "\n" +
//                "lowp float maxValue = max(centerColor.r, bottomColor);\n" +
//                "maxValue = max(maxValue, bottomRightColor);\n" +
//                "maxValue = max(maxValue, rightColor);\n" +
//                "maxValue = max(maxValue, topRightColor);\n" +
//                "\n" +
//                "gl_FragColor = vec4((centerColor.rgb * step(maxValue, centerColor.r) * multiplier), 1.0);\n" +
                "lowp float theta;\n"+
                "if(centerColor.x != 0.0)\n"+
                "theta =  atan(centerColor.z/centerColor.y);\n"+
                "else\n"+
                "theta=90.0;\n"+
                "gl_FragColor = centerColor;//vec4(vec3(sqrt(centerColor.x*centerColor.x+centerColor.y*centerColor.y)),1.0);\n"+
                "}\n";
        final static String vert =
                "uniform sampler2D inputImageTexture;\n" +

                "attribute vec4 inputTextureCoordinate;\n" +
                "attribute vec4 position;\n" +

                "uniform highp float texelWidth; \n" +
                "uniform highp float texelHeight; \n" +

                "varying vec2 textureCoordinate;\n" +
                "void main()\n" +
                "{\n" +
                "    textureCoordinate = inputTextureCoordinate.xy;\n" +
                "    gl_Position = position;\n" +
                "}";

        final static String frag =
                "varying vec2 textureCoordinate;\n" +
                        "uniform sampler2D inputImageTexture;\n" +
                "void main(){\n" +
                "gl_FragColor = texture2D(inputImageTexture, textureCoordinate);"+
                "}";
    public GPUImageCannyEdgeDet() {
        super(THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER,SOBEL_EDGE_DETECTION, THREE_X_THREE_TEXTURE_SAMPLING_VERTEX_SHADER,frag);
    }
}