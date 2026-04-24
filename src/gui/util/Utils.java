package gui.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class Utils {

	public static Stage currentStage(ActionEvent event) {
		return (Stage) ((Node) event.getSource()).getScene().getWindow();
	}

	public static Integer tryParseToInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public static Double tryParseToDouble(String str) {
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static <T> void formatTableColumnDate(TableColumn<T, LocalDate> tableColumn, String format) {
	    tableColumn.setCellFactory(column -> {
	        TableCell<T, LocalDate> cell = new TableCell<T, LocalDate>() {
	            private DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);

	            @Override
	            protected void updateItem(LocalDate item, boolean empty) {
	                super.updateItem(item, empty);
	                if (empty || item == null) {
	                    setText(null);
	                } else {
	                    setText(dtf.format(item));
	                }
	            }
	        };
	        return cell;
	    });
	}
	
	public static <T> void formatTableColumnDateTime(TableColumn<T, LocalDateTime> tableColumn, String format) {
	    tableColumn.setCellFactory(column -> {
	        TableCell<T, LocalDateTime> cell = new TableCell<T, LocalDateTime>() {
	            private DateTimeFormatter dtf = DateTimeFormatter.ofPattern(format);

	            @Override
	            protected void updateItem(LocalDateTime item, boolean empty) {
	                super.updateItem(item, empty);
	                if (empty || item == null) {
	                    setText(null);
	                } else {
	                    setText(dtf.format(item));
	                }
	            }
	        };
	        return cell;
	    });
	}


	public static <T> void formatTableColumnDouble(TableColumn<T, Double> tableColumn, int decimalPlaces) {
		tableColumn.setCellFactory(column -> {
			TableCell<T, Double> cell = new TableCell<T, Double>() {
				@Override
				protected void updateItem(Double item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setText(null);
					} else {
						Locale.setDefault(Locale.US);
						setText(String.format("%." + decimalPlaces + "f", item));
					}
				}
			};
			return cell;
		});
	}
	

	public static <T> void formatTableColumnBigDecimal(TableColumn<T, BigDecimal> tableColumn, int decimalPlaces) {
	    tableColumn.setCellFactory(column -> {
	        return new TableCell<T, BigDecimal>() {
	            @Override
	            protected void updateItem(BigDecimal item, boolean empty) {
	                super.updateItem(item, empty);
	 
	                if (empty || item == null) {
	                    setText(null);
	                } else {
	                    setText(String.format(Locale.US, "%." + decimalPlaces + "f", item));
	                }
	            }
	        };
	    });
	}
	
	public static <T> void formatTableColumnStringCamelCase(TableColumn<T, String> tableColumn) {
		tableColumn.setCellFactory(column ->{
			return new TableCell<T, String>(){
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					
					if(empty || item == null) {
						setText(null);
					}
					else {
						String[] words = item.toLowerCase().split("_");
						StringBuilder result = new StringBuilder();
						
						for(String word : words) {
							if(!word.isEmpty()) {
							result.append(Character.toUpperCase(word.charAt(0)))
		                     .append(word.substring(1))
		                     .append(" ");
							}
						}
						setText(result.toString().trim());
					}
				}
			};
		});
	}
	
	public static <T> void formatTableColumnRowAsIndex(TableColumn<T, Integer> tableColumn) {
		tableColumn.setCellFactory(column ->{
			return new TableCell<T, Integer>(){
				@Override
				protected void updateItem(Integer item, boolean empty) {
					super.updateItem(item, empty);
					
					if(empty) {
						setText(null);
					}
					else {
						setText(String.valueOf(getIndex() + 1));
					}
				}
			};
		});
	}

	public static void formatDatePicker(DatePicker datePicker, String format) {
		datePicker.setConverter(new StringConverter<LocalDate>() {
			DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(format);
			{
				datePicker.setPromptText(format.toLowerCase());
			}

			@Override
			public String toString(LocalDate date) {
				if (date != null) {
					return dateFormatter.format(date);
				} else {
					return "";
				}
			}

			@Override
			public LocalDate fromString(String string) {
				if (string != null && !string.isEmpty()) {
					return LocalDate.parse(string, dateFormatter);
				} else {
					return null;
				}
			}
		});
	}
}
