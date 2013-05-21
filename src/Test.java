/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author 13thompsona
 */
public class Test {
    public static void main(String[] args) {
        GameConfig test = new GameConfig("test.json");
        test.dumpJson();
        System.out.println(test.getValueAsInt("test"));
    }
}