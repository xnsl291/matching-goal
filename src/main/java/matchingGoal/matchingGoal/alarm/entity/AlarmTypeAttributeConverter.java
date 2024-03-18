package matchingGoal.matchingGoal.alarm.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
//TODO exception 지정
@Converter
public class AlarmTypeAttributeConverter implements AttributeConverter<String, Integer> {

  @Override
  public Integer convertToDatabaseColumn(String attribute) {
    if ("chat".equals(attribute)) {
      return 1;
    } else if ("newMatchingRequest".equals(attribute)) {
      return 2;
    } else if ("matchingRequestDenied".equals(attribute)) {
      return 3;
    } else if ("matchingRequestAccepted".equals(attribute)) {
      return 4;
    }
    throw new RuntimeException();
  }

  @Override
  public String convertToEntityAttribute(Integer dbData) {
    if (1 == dbData) {
      return "chat";
    } else if (2 == dbData) {
      return "newMatchingRequest";
    } else if (3 == dbData) {
      return "matchingRequestDenied";
    } else if (4 == dbData) {
      return "matchingRequestAccepted";
    }
     throw new RuntimeException();
  }
}
