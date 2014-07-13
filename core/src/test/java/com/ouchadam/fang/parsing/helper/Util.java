package com.ouchadam.fang.parsing.helper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Util {

    public static List<String> categoryList(String category, String... categories) {
        List<String> output = new ArrayList<String>();
        output.add(category);
        output.addAll(Arrays.asList(categories));
        return output;
    }

}
