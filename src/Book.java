

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.ToDoubleBiFunction;

public class Book {
    //    constant for default values
    private static final int UNKNOWN_INT = -1;
    private static final String UNKNOWN_STRING = "Unknown";
    private static final LocalDate UNKNOWN_DATE = null;
//    TODO: I could generate the Id at the instantiation of any book object so that all books will have an ID

    //  Book Atrributes
    private int id;
    private String title;
    private String author;
    private LocalDate publicationDate;
    private String genre;
    private HashMap<Integer, Book> bookRecordsById = new HashMap<>();
    HashMap<String, ArrayList<Book>> bookRecordsByTitle = new HashMap<>();
    private HashMap<String, ArrayList<Book>> bookRecordsByAuthor = new HashMap<>();
    private HashMap<String, ArrayList<Book>> bookRecordsByGenre = new HashMap<>();
    private ArrayList<Book> books = new ArrayList<>();

    //    Default Constructor
//    learnt about constructor chaining
    public Book() {
//TODO: what if I did not want to type multiple Unknown? could I declare it as a variable. yes, you declare as constants
        this(UNKNOWN_INT, UNKNOWN_STRING, UNKNOWN_STRING, UNKNOWN_DATE, UNKNOWN_STRING);
    }

    //main constructor
    public Book(int id, String title, String author, LocalDate publicationDate, String genre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.publicationDate = publicationDate;
        this.genre = genre;
        addBookToRecords();
//        how can I persist this data by adding to a file? Also, are files searchable by key? Will i be saving the different hashmaps to a file? just thinking of ways to persist the data in a file, so that I can still access it even after rebooting my laptop.
    }

    public Book(int id, String title, String author, LocalDate publicationDate) {
        this(id, title, author, publicationDate, UNKNOWN_STRING);

    }

    public Book(int id, String title, String author) {
        this(id, title, author, UNKNOWN_DATE, UNKNOWN_STRING);
    }

    public Book(int id, String title) {
        this(id, title, UNKNOWN_STRING, UNKNOWN_DATE, UNKNOWN_STRING);
    }

    public Book(int id) {
        this(id, UNKNOWN_STRING, UNKNOWN_STRING, UNKNOWN_DATE, UNKNOWN_STRING);
    }

    public Book(String title) {
        this(UNKNOWN_INT, title, UNKNOWN_STRING, UNKNOWN_DATE, UNKNOWN_STRING);
    }

    //    getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDate publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

     /*
        TODO: after removing a book using any identifier, I need to check other locations where
         the book can be stored and remove it too
         for now we can only remove by id. When a book is removed by Id, it should be able to other data stores like booksByAuthor and delete


         */
    public void removeBook(String id) {

        if (bookRecordsById.containsKey(id)) {
            bookRecordsById.remove(id);
            System.out.println("Deletion successful");
        } else {
            System.out.printf("%d does not exist hence no deletion", id);
        }


    }
//    TODO: To implement searching using other identifiers asides id
    public Book searchBook(String availableParameter) {
        if (bookRecordsById.containsKey(availableParameter)) {
            return bookRecordsById.get(availableParameter);
        }
        System.out.printf("There is no book with id of %d \n", id);
        return null;
    }

    public void addBookToRecords() {
        bookRecordsById.computeIfAbsent(id, k -> this);
        bookRecordsByAuthor.computeIfAbsent(author, k -> new ArrayList<>()).add(this);
        bookRecordsByGenre.computeIfAbsent(genre, k -> new ArrayList<>()).add(this);
        bookRecordsByTitle.computeIfAbsent(title, k -> new ArrayList<>()).add(this);
        books.add(this);

    }

    @Override
    public String toString() {
        return "The book id is " + id +
                ", title is " + title +
                ", author is " + author +
                ", publication date is " + publicationDate +
                ", genre is " + genre;

    }

    public void saveBookDataToFile(File file) throws FileNotFoundException {
        System.out.println("Book records is being saved");

        try (
            FileOutputStream fileStream = new FileOutputStream(file);
            ObjectOutputStream objectStream = new ObjectOutputStream(fileStream);
        )
            {
            objectStream.writeObject(id);
            objectStream.writeObject(title);
            objectStream.writeObject(author);
            objectStream.writeObject(publicationDate);
            objectStream.writeObject(genre);
            objectStream.writeObject(bookRecordsById);
            objectStream.writeObject(bookRecordsByTitle);
            objectStream.writeObject(bookRecordsByAuthor);
            objectStream.writeObject(bookRecordsByGenre);
            objectStream.writeObject(books);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @SuppressWarnings("unchecked")
    public void readBookRecords(File file) {
        if (!file.exists()) {
            System.out.println("Loading book records form file");
            return;
        }
        try  (
            FileInputStream fileStream = new FileInputStream(file);
            ObjectInputStream objectStream = new ObjectInputStream(fileStream);

        )
        {
            id = (int) objectStream.readObject();
            title = (String) objectStream.readObject();
            author = (String) objectStream.readObject();
            publicationDate = (LocalDate) objectStream.readObject();
            genre = (String) objectStream.readObject();
            bookRecordsById = (HashMap<Integer, Book>) objectStream.readObject();
            bookRecordsByTitle = (HashMap<String, ArrayList<Book>>) objectStream.readObject();
            bookRecordsByAuthor = (HashMap<String, ArrayList<Book>>) objectStream.readObject();
            bookRecordsByGenre = (HashMap<String, ArrayList<Book>>) objectStream.readObject();
            books = (ArrayList<Book>) objectStream.readObject();

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Caught in  load book records");
            e.printStackTrace();

        } 
    }

}

