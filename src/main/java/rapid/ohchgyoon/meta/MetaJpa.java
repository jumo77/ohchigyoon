package rapid.ohchgyoon.meta;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MetaJpa extends JpaRepository<MetaData, Integer> {
    boolean existsByFileName(String fileName);
    @Query(value = "select distinct m.category from MetaData m")
    List<String> findDistinctCategory();
    MetaData findByCategory(String category);
    MetaData findDistinctTopByOrderById();
}
