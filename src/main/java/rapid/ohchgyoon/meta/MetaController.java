package rapid.ohchgyoon.meta;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rapid.ohchgyoon.Literals;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class MetaController {

    private final MetaJpa metaJpa;

    @GetMapping(value = "/head")
    public List<MetaData> head(){
        List<MetaData> dataList = new ArrayList<>();
        metaJpa.findDistinctCategory().forEach(it->
                dataList.add(metaJpa.findByCategory(it)));
        return dataList;
    }

    @PostMapping(value = "/upload")
    public String upload(@RequestBody MetaData metaData){
        if (metaJpa.existsByFileName(metaData.getFileName())) return "이미 존재하는 파일입니다.";
        metaJpa.save(metaData);
        return "저장합니다.";
    }

    @PostMapping(value = "/upload/file", produces = MediaType.IMAGE_PNG_VALUE)
    public String uploadFile(@RequestPart(value = "file", required = false) MultipartFile image) throws Exception {

        File folder = new File(Literals.PATH);
        if (!folder.exists()) folder.mkdir();
        if (image.getOriginalFilename() == null) return "파일 이름을 확인해주세요.";
        image.transferTo(new File(folder, image.getOriginalFilename()));
        return "저장되었습니다.";
    }

    @PostMapping("/search")
    public List<MetaData> search(@RequestBody String keywords){
        return metaJpa.findAll().stream()
                .filter(metaData -> metaData.includes(List.of(keywords.split(" "))))
                .toList();
    }

    @GetMapping("/bg")
    public ResponseEntity<?> bg(){
        return new ResponseEntity<>(new FileSystemResource(Literals.PATH + "/" +
                metaJpa.findDistinctTopByOrderById().getFileName()), HttpStatus.OK);
    }

    @GetMapping(value = "/{fileName}")
    public ResponseEntity<?> get(@PathVariable("fileName") String fileName){
        return new ResponseEntity<>(new FileSystemResource(Literals.PATH + "/" + fileName), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable(value = "id") Integer id){
        String err = "삭제 처리 과정 중 문제가 발생했습니다.";

        if (id==null) return err;
        File file = new File(Literals.PATH+"/"+metaJpa.findById(id).get().getFileName());
        if (!metaJpa.existsById(id)) return err;
        metaJpa.deleteById(id);
        boolean d = file.delete();
        if (!d) return err;

        return "삭제되었습니다.";
    }

    //vsftpd passive 설정

}
