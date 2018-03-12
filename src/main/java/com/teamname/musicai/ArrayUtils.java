package com.teamname.musicai;

public class ArrayUtils {

    public static float[] toPrimitiveFloat(Float[] array) {
        //todo validate on null
        float[] result = new float[array.length];

        for (int i = 0; i < array.length; i++) {
            result[i] = array[i] == null ? 0f : array[i];
        }

        return result;


    }

}
