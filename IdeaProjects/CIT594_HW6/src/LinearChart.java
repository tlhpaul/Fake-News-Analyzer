import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import java.util.ArrayList;

/**
 * Interface for linear data charts that creates datasets
 *
 */
public interface LinearChart {

    /**
     * Creates the dataset for the linear chart
     *
     * @param values to be added to the chart
     * @param comparatorList is the list of comparator entities in the dataset
     * @return the CategoryDataset to be used in the linear chart
     */
    static CategoryDataset createDataset(ArrayList<ArrayList<Integer>> values, ArrayList<String> comparatorList) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        int comparatorIndex =0;
        for(ArrayList<Integer> valueSet: values) {

            String comparator = comparatorList.get(comparatorIndex);
            for (int i = 0; i < valueSet.size(); i++) {
                dataset.addValue(valueSet.get(i), comparator, Integer.toString(i));
            }

            comparatorIndex ++;
        }
        return dataset;
    }

}
