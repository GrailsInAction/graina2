class CopingWithLineBreaks {
    static void main(String[] args)
    {
        CopingWithLineBreaks obj = new CopingWithLineBreaks()
        obj.invalidMethod("Tarzan")
        obj.validMethod("Jane")
    }

    /*
     * Add continuation characters to make this method compile.
     */
    private void invalidMethod
            (String name) {
        String fruit = "orange, apple, pear, "
            + "banana, cherry, nectarine"
        println "Fruit: $fruit"
    }

    private int validMethod(
            String name) {
        String furniture = "table, chair, sofa, bed, " +
            "cupboard, wardrobe, desk"
        println "Furniture: $furniture"

        String fruit = "orange, apple, pear, " \
            + "banana, cherry, nectarine"
        println "Fruit: $fruit"

        int i = 0; i++; return i;
    }
}
