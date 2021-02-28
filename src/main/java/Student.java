public class Student {
    private String id;
    private String name;
    private String enteredAt;

    public Student(String _id, String _name, String _enteredAt){
        id = _id;
        name = _name;
        enteredAt = _enteredAt;
    }

    public String getId(){
        return id;
    }
}
