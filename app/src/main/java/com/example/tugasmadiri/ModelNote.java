package com.example.tugasmadiri;

public class ModelNote {
    private String id;
    private String title;
    private String description;
    private String dataNoteAdded;


    public ModelNote()
    {

    }

    public ModelNote(String id, String title, String description, String dataNoteAdded)
    {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dataNoteAdded = dataNoteAdded;

    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public String getDataNoteAdded()
    {
        return dataNoteAdded;
    }

    public void setDataNoteAdded(String dataNoteAdded)
    {
        this.dataNoteAdded = dataNoteAdded;
    }

}
