package iojjj.android.bootstrap.database.annotations.processor;

import com.google.auto.service.AutoService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import iojjj.android.bootstrap.database.annotations.Collate;
import iojjj.android.bootstrap.database.annotations.Column;
import iojjj.android.bootstrap.database.annotations.Index;
import iojjj.android.bootstrap.database.annotations.OnConflictClause;
import iojjj.android.bootstrap.database.annotations.Table;
import iojjj.android.bootstrap.database.annotations.TypeConverter;
import javafx.util.Pair;

@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private static final String DEFAULT_ID = "_id";

    private static final String TYPE_INTEGER = "INTEGER";
    private static final String TYPE_REAL = "REAL";
    private static final String TYPE_NUMERIC = "NUMERIC";
    private static final String TYPE_TEXT = "TEXT";
    private static final String TYPE_BLOB = "BLOB";

    private static final Map<String, String> TYPES_MAP = new HashMap<>();
    private Messager messager;
    private Elements elementUtils;
    private Types typeUtils;

    static {
        TYPES_MAP.put(long.class.getSimpleName(), TYPE_INTEGER);
        TYPES_MAP.put(int.class.getSimpleName(), TYPE_INTEGER);
        TYPES_MAP.put(short.class.getSimpleName(), TYPE_INTEGER);
        TYPES_MAP.put(byte.class.getSimpleName(), TYPE_INTEGER);
        TYPES_MAP.put(boolean.class.getSimpleName(), TYPE_NUMERIC);
        TYPES_MAP.put(float.class.getSimpleName(), TYPE_REAL);
        TYPES_MAP.put(double.class.getSimpleName(), TYPE_REAL);
        TYPES_MAP.put(char.class.getSimpleName(), TYPE_TEXT);
        TYPES_MAP.put(byte[].class.getSimpleName(), TYPE_BLOB);
        TYPES_MAP.put(Integer.class.getCanonicalName(), TYPE_INTEGER);
        TYPES_MAP.put(Long.class.getCanonicalName(), TYPE_INTEGER);
        TYPES_MAP.put(Short.class.getCanonicalName(), TYPE_INTEGER);
        TYPES_MAP.put(Byte.class.getCanonicalName(), TYPE_INTEGER);
        TYPES_MAP.put(Boolean.class.getCanonicalName(), TYPE_NUMERIC);
        TYPES_MAP.put(Float.class.getCanonicalName(), TYPE_REAL);
        TYPES_MAP.put(Double.class.getCanonicalName(), TYPE_REAL);
        TYPES_MAP.put(Character.class.getCanonicalName(), TYPE_TEXT);
        TYPES_MAP.put(Byte[].class.getCanonicalName(), TYPE_BLOB);
        TYPES_MAP.put(String.class.getCanonicalName(), TYPE_TEXT);
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        messager = processingEnv.getMessager();
        elementUtils = processingEnv.getElementUtils();
        typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> set = new HashSet<>();
        set.add(Table.class.getCanonicalName());
        set.add(Column.class.getCanonicalName());
        set.add(Index.class.getCanonicalName());
        set.add(TypeConverter.class.getCanonicalName());
        return set;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(Table.class)) {
            processTable(element);
        }
        return true;
    }

    private void processTable(Element element) {
        Table table = element.getAnnotation(Table.class);
        String tableName = table.name();
        String primaryKey = join(table.primaryKey(), ", ");
        if (isEmpty(tableName))
            tableName = element.getSimpleName().toString();
        if (isEmpty(primaryKey))
            primaryKey = DEFAULT_ID;
        if (element.getModifiers() != null && element.getModifiers().contains(Modifier.ABSTRACT)) {
            return;
        }
        List<Pair<Column, Element>> columns = new ArrayList<>();
        List<Pair<Index, Element>> indexes = new ArrayList<>();
        TypeMirror superclass = ((TypeElement) element).getSuperclass();
        while (superclass.getKind() != TypeKind.NONE) {
            List<? extends Element> elementList = elementUtils.getAllMembers((TypeElement) element);
            for (Element e : elementList) {
                if (e.getKind() == ElementKind.FIELD && !e.getModifiers().contains(Modifier.STATIC)) {
                    Column column = e.getAnnotation(Column.class);
                    if (column != null)
                        columns.add(new Pair<>(column, e));
                    Index index = e.getAnnotation(Index.class);
                    if (index != null)
                        indexes.add(new Pair<>(index, e));
                }
            }
            element = typeUtils.asElement(superclass);
            superclass = ((TypeElement) element).getSuperclass();
        }
        Collections.reverse(columns);
        Collections.reverse(indexes);
        StringBuilder columnsBuilder = new StringBuilder();
        for (Pair<Column, Element> pair : columns) {
            String colName = pair.getKey().name();
            if (isEmpty(colName))
                colName = pair.getValue().getSimpleName().toString();
            String type = TYPES_MAP.get(pair.getValue().asType().toString());
            if (isEmpty(type))
                messager.printMessage(Diagnostic.Kind.ERROR, "invalid column type: " + colName + ", " + pair.getValue().asType().toString());
            columnsBuilder.append(colName)
                    .append(" ")
                    .append(type);
            if (pair.getKey().notNull()) {
                columnsBuilder.append(" ")
                        .append(" NOT NULL");
                if (pair.getKey().notNullConflict() != OnConflictClause.DEFAULT) {
                    columnsBuilder
                            .append(" ON CONFLICT ")
                            .append(pair.getKey().notNullConflict().name());
                }
            }
            if (pair.getKey().unique()) {
                columnsBuilder.append(" ")
                        .append(" UNIQUE");
                if (pair.getKey().uniqueConflict() != OnConflictClause.DEFAULT) {
                    columnsBuilder
                            .append(" ON CONFLICT ")
                            .append(pair.getKey().uniqueConflict().name());
                }
            }
            String defaultValue = pair.getKey().defaultValue();
            if (!isEmpty(defaultValue)) {
                columnsBuilder.append(" DEFAULT '")
                        .append(defaultValue)
                        .append("'");
            }
            if (pair.getKey().collate() != Collate.NONE) {
                columnsBuilder.append(" COLLATE ")
                        .append(pair.getKey().collate().name());
            }
            columnsBuilder.append(", ");
        }
        columnsBuilder.append("PRIMARY KEY (")
                .append(primaryKey)
                .append(")");
        for (Pair<Index, Element> pair : indexes) {
            String indexName = pair.getKey().name();
            if (isEmpty(indexName)) {
                indexName = generateIndexName(pair.getValue().getSimpleName().toString());
            }
            messager.printMessage(Diagnostic.Kind.ERROR, "index: " + indexName);
        }

        messager.printMessage(Diagnostic.Kind.ERROR, "CREATE TABLE " + tableName + "(" + columnsBuilder.toString() + ")");
    }

    private static boolean isEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }

    private static String generateIndexName(String fieldName) {
        return "index_" + fieldName;
    }

    public static String join(String[] aArr, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = 0, il = aArr.length; i < il; i++) {
            if (i > 0)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }
}
