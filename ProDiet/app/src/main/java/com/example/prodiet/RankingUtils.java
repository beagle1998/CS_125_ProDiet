package com.example.prodiet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

public class RankingUtils {

    /**
     * Sort list of food based on frequency in food choice history
     * and nutritional value (less fat possible), descending order
     */
    public static void rankFoodList(ArrayList<Food> food_list, final Map<?, ?> history_map) {
        Collections.sort(food_list, new Comparator<Food>() {

            @Override
            public int compare(final Food f1, final Food f2) {
                Long f1freq = (Long)history_map.get(f1.getFoodName());
                if (f1freq == null) {
                    f1freq = 0L;
                }
                Long f2freq = (Long)history_map.get(f2.getFoodName());
                if (f2freq == null) {
                    f2freq = 0L;
                }

                int c = -(f1freq).compareTo(f2freq);
                if (c == 0) {
                    c = f1.getFat().compareTo(f2.getFat());
                }
//                if (c == 0) {
//                    c = -f1.getProtein().compareTo(f2.getProtein());
//                }
                return c;
            }

        });
    }
}
