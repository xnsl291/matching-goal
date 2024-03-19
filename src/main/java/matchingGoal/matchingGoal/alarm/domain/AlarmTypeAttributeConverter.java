package matchingGoal.matchingGoal.alarm.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.stream.Stream;

//TODO exception 지정
@Converter
public class AlarmTypeAttributeConverter implements AttributeConverter<AlarmType, Integer> {

  @Override
  public Integer convertToDatabaseColumn(AlarmType attribute) {
    if (attribute == null) {
      throw new RuntimeException();
    }
    return attribute.getCode();
  }

  @Override
  public AlarmType convertToEntityAttribute(Integer code) {
    if (code == null) {
      throw new RuntimeException();
    }

    return Stream.of(AlarmType.values())
        .filter(c -> c.getCode() == code)
        .findFirst()
        .orElseThrow(IllegalAccessError::new);
  }

}
