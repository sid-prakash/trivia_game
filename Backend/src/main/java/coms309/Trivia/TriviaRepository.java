package coms309.Trivia;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TriviaRepository extends JpaRepository<Trivia, Long> {

    Trivia findById(int id);

    boolean existsByQuestion(String question);

    boolean existsById(int id);

    @Query(value = "select id from trivia", nativeQuery = true)
    List<Integer> getAllIds();

    @Query(value = "select id from trivia where question_theme = :theme", nativeQuery = true)
    List<Integer> getAllIdsByQuestionTheme(@Param("theme") String theme);

    @Query(value = "select * from trivia where question_theme = :question_theme", nativeQuery = true)
    List<Trivia> findAllByQuestionTheme(@Param("question_theme") String questionTheme);
}
