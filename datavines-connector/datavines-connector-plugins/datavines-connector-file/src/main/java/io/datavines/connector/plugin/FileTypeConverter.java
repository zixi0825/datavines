package io.datavines.connector.plugin;

import io.datavines.common.enums.DataType;
import io.datavines.common.utils.StringUtils;
import io.datavines.connector.api.TypeConverter;

public class FileTypeConverter implements TypeConverter {

    @Override
    public DataType convert(String originType) {
        if (StringUtils.isEmpty(originType)) {
            throw new UnsupportedOperationException("sql type id null error");
        }

        switch (originType.toUpperCase()) {
            case "NULL":
                return DataType.NULL_TYPE;
            case "BOOLEAN":
                return DataType.BOOLEAN_TYPE;
            case "BIT":
            case "TINYINT":
                return DataType.BYTE_TYPE;
            case "TINYINT_UNSIGNED":
            case "SMALLINT":
                return DataType.SHORT_TYPE;
            case "SMALLINT_UNSIGNED":
            case "INT":
            case "MEDIUMINT":
            case "MEDIUMINT_UNSIGNED":
                return DataType.INT_TYPE;
            case "INT_UNSIGNED":
            case "BIGINT":
                return DataType.LONG_TYPE;
            case "FLOAT":
            case "FLOAT_UNSIGNED":
                return DataType.FLOAT_TYPE;
            case "DOUBLE":
            case "DOUBLE_UNSIGNED":
                return DataType.DOUBLE_TYPE;
            case "TIME":
                return DataType.TIME_TYPE;
            case "DATE":
                return DataType.DATE_TYPE;
            case "TIMESTAMP":
            case "DATETIME":
                return DataType.TIMESTAMP_TYPE;
            // TODO: to confirm
            case "CHAR":
            case "VARCHAR":
            case "TINYTEXT":
            case "TEXT":
            case "MEDIUMTEXT":
            case "LONGTEXT":
            case "JSON":
            case "ENUM":
            case "STRING":
                return DataType.STRING_TYPE;
            case "BINARY":
            case "VARBINARY":
            case "TINYBLOB":
            case "BLOB":
            case "MEDIUMBLOB":
            case "LONGBLOB":
            case "GEOMETRY":
                return DataType.BYTES_TYPE;
            case "BIGINT_UNSIGNED":
            case "DECIMAL":
            case "DECIMAL_UNSIGNED":
                return DataType.BIG_DECIMAL_TYPE;
            default:
                throw new UnsupportedOperationException(String.format("Doesn't support sql type '%s' yet", originType));
        }
    }

    @Override
    public String convertToOriginType(DataType dataType) {
        switch (dataType) {
            case TIME_TYPE:
            case DATE_TYPE:
            case TIMESTAMP_TYPE:
            case NULL_TYPE:
            case STRING_TYPE:
                return "TEXT";
            case BYTE_TYPE:
            case BOOLEAN_TYPE:
                return "TINYINT";
            case SHORT_TYPE:
                return "SMALLINT";
            case DOUBLE_TYPE:
                return "DOUBLE";
            case FLOAT_TYPE:
                return "FLOAT";
            case BIG_DECIMAL_TYPE:
                return "DECIMAL";
            case INT_TYPE:
            case LONG_TYPE:
                return "BIGINT";
            case BYTES_TYPE:
            case OBJECT:
                return "LONGBLOB";
            default:
                return "TEXT";
        }
    }
}
