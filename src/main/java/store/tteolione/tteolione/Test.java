package store.tteolione.tteolione;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Test {

    @Id
    @GeneratedValue
    private int id;

    @Column
    private String name;
}
