package rapid.ohchgyoon.meta;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "art_table")
public class MetaData {

    @Id
    @GeneratedValue
    private Integer id;
    @Column(name = "file_name")
    private String fileName;
    private String name;
    private String description;
    private String date;
    private String category;
    private String material;
    private Integer width;
    private Integer height;

    public boolean includes(List<String> keywords){
        for (String key: keywords){
            if (fileName.contains(key)) return true;
            if (name.contains(key)) return true;
            if (description.contains(key)) return true;
            if (date.contains(key)) return true;
            if (category.contains(key)) return true;
            if (material.contains(key)) return true;
            if (width==Integer.parseInt(key)) return true;
            if (height==Integer.parseInt(key)) return true;
        }
        return false;
    }
}
