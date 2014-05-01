class CopingWithLineBreaks {
    static void main(String[] args)
    {
        CopingWithLineBreaks obj = new CopingWithLineBreaks()
        obj.invalidMethod("Tarzan")
        obj.validMethod("Jane")
    }

    /*
     * The starting parenthesis <em>has</em> to be on the same line as the
     * method name. The fixed version is commented out below the offending
     * line.
     */
    private void invalidMethod
            (String name) {
//    private void invalidMethod(
//            String name) {

        // Groovy can't tell that we are concatenating two strings together
        // here because the first line is a complete statement. To fix the
        // resulting compilation error, move the '+' to the end of the first
        // line. You can still keep the "banana, cherry,..." string on its
        // own line - it's the position of the '+' that's crucial.
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
