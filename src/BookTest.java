public class BookTest {

    public static void main(String[] args) {
//        create a book
        Book myFirstBook = new Book();


        myFirstBook.setTitle("The Great Gatsby");
        System.out.println(myFirstBook);

        myFirstBook.bookRecordsByTitle.forEach((key, value) -> System.out.print("Key:" + key + "Value: "+  value));
        myFirstBook.toString();
//        System.out.printf("The title of my myFirstBook is %s \n", myFirstBook.getTitle());

    }
}
