package com.maoding.CoreUtils;

import com.maoding.Base.BaseEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* BeanUtils Tester. 
* 
* @author Zhangchengliang
* @since 10/27/2017 
* @version 1.0 
*/
class Father {
    private Integer i;


    public Father(){}
    public Father(Integer i){
        this.i = i;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }
}

class Child extends Father {
    private String s;

    public Child(){}
    public Child(Integer i, String s){
        super(i);
        this.s = s;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }
}

class TestClass1 {
    private int digital;
    private Long objectDigital;
    private String string;
    private Father object;
    private byte[] array;
    private BaseEntity entity;
    private List<Integer> list;

    public List<Integer> getList() {
        return list;
    }

    public void setList(List<Integer> list) {
        this.list = list;
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }

    public byte[] getArray() {
        return array;
    }

    public void setArray(byte[] array) {
        this.array = array;
    }

    public Father getObject() {
        return object;
    }

    public void setObject(Father object) {
        this.object = object;
    }

    public int getDigital() {
        return digital;
    }

    public void setDigital(int digital) {
        this.digital = digital;
    }

    public Long getObjectDigital() {
        return objectDigital;
    }

    public void setObjectDigital(Long objectDigital) {
        this.objectDigital = objectDigital;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }
}

class TestClass2 {
    private float digital;
    private Double objectDigital;
    private StringBuffer string;
    private Child object;
    private Byte[] array;
    private BaseEntity entity;
    private List<Float> list;

    public List<Float> getList() {
        return list;
    }

    public void setList(List<Float> list) {
        this.list = list;
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }

    public Byte[] getArray() {
        return array;
    }

    public void setArray(Byte[] array) {
        this.array = array;
    }

    public Child getObject() {
        return object;
    }

    public void setObject(Child object) {
        this.object = object;
    }

    public float getDigital() {
        return digital;
    }

    public void setDigital(float digital) {
        this.digital = digital;
    }

    public Double getObjectDigital() {
        return objectDigital;
    }

    public void setObjectDigital(Double objectDigital) {
        this.objectDigital = objectDigital;
    }

    public StringBuffer getString() {
        return string;
    }

    public void setString(StringBuffer string) {
        this.string = string;
    }
}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration
@ComponentScan(basePackages = {"com.maoding.CoreUtils"})
public class BeanUtilsTest {

    @Test
    public void testCreateMapFrom() throws Exception {
        TestClass2 src = new TestClass2();
        src.setDigital(3.2f);
        src.setObjectDigital(5.2);
        src.setString(new StringBuffer("class2"));
        src.setObject(new Child(1,"child"));
        src.setArray(new Byte[]{1,2,3,null});
        src.setEntity(new BaseEntity());
        src.setList(new ArrayList<>());
        src.getList().add(1.1f);
        Map<String,Object> des = BeanUtils.createMapFrom(src,String.class,Object.class);
        Assert.assertNotNull(des);
    }

    @Test
    public void testCreateListFrom() throws Exception {
        List<Integer> src = new ArrayList<>();
        src.add(1);
        src.add(null);
        List<Float> des = BeanUtils.createListFrom(src,Float.class);
        Assert.assertNotNull(des);
        List<Short> des2 = BeanUtils.createListFrom(des,Short.class);
        Assert.assertNotNull(des2);
    }

    @Test
    public void testCreateFromSpeed() throws Exception {
        long t = System.currentTimeMillis();
        for (int i=0; i<10000; i++) {
            createFromClass1ToClass2();
        }
        System.out.println("===>testCreateFrom:" + (System.currentTimeMillis()-t) + "ms");
    }

    @Test
    public void testCreateFrom() throws Exception {
        createFromFatherToChild();
        createFromChildToFather();
        createFromClass1ToClass2();
        createFromClass2ToClass1();
        createFromMapToClass1();
        createFromMapToClass2();
    }

    private void createFromMapToClass1(){
        Map<String,Object> src = new HashMap<>();
        src.put("Digital",3.5);
        src.put("ObjectDigital",5.5);
        src.put("String","class2");
        src.put("Object",new Child(2,"child"));
        src.put("Array",new Byte[]{1,2,3,null});
        src.put("Entity",new BaseEntity());
        List<Float> list = new ArrayList<>();
        list.add(1.5f);
        src.put("List",list);
        TestClass1 des = BeanUtils.createFrom(src,TestClass1.class);
        Assert.assertNotNull(des);
    }

    private void createFromMapToClass2(){
        Map<String,Object> src = new HashMap<>();
        src.put("Digital",3);
        src.put("ObjectDigital",5L);
        src.put("String","class1");
        src.put("Object",new Father(1));
        src.put("Array",new byte[]{1,2,3});
        src.put("Entity",new BaseEntity());
        List<Integer> list = new ArrayList<>();
        list.add(1);
        src.put("List",list);
        TestClass2 des = BeanUtils.createFrom(src,TestClass2.class);
        Assert.assertNotNull(des);
    }

    private void createFromClass1ToClass2(){
        TestClass1 src = new TestClass1();
        src.setDigital(3);
        src.setObjectDigital(5L);
        src.setString("class1");
        src.setObject(new Father(1));
        src.setArray(new byte[]{1,2,3});
        src.setEntity(new BaseEntity());
        src.setList(new ArrayList<>());
        src.getList().add(1);
        TestClass2 des = BeanUtils.createFrom(src,TestClass2.class);
        Assert.assertNotNull(des);
    }

    private void createFromClass2ToClass1(){
        TestClass2 src = new TestClass2();
        src.setDigital(3.2f);
        src.setObjectDigital(5.2);
        src.setString(new StringBuffer("class2"));
        src.setObject(new Child(1,"child"));
        src.setArray(new Byte[]{1,2,3,null});
        src.setEntity(new BaseEntity());
        src.setList(new ArrayList<>());
        src.getList().add(1.1f);
        TestClass1 des = BeanUtils.createFrom(src,TestClass1.class);
        Assert.assertNotNull(des);
    }

    private void createFromFatherToChild(){
        Father src = new Father(1);
        Child des = BeanUtils.createFrom(src,Child.class);
        Assert.assertEquals(src.getI(),des.getI());
    }

    private void createFromChildToFather(){
        Child src = new Child(1,"child");
        Father des = BeanUtils.createFrom(src,Father.class);
        Assert.assertEquals(src.getI(),des.getI());
    }

    /** for method: copyProperties(final Map<String, Object> input, final Object output) */
    @Test
    public void testCopyPropertiesForInputOutput() throws Exception { 
    }

    /** for method: getProperty(final Object obj, final String ptyName) */ 
    @Test
    public void testGetProperty() throws Exception { 

    }

    @Test
    public void testCleanProperties() throws Exception {
    }

    /** action before each test */
    @Before
    public void before() throws Exception { 
    } 
    
    /** action after every test */
    @After
    public void after() throws Exception { 
    } 
} 
