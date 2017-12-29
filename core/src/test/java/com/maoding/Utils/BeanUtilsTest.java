package com.maoding.Utils;

import com.maoding.Base.BaseEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/** 
* BeanUtils Tester. 
* 
* @author Zhangchengliang
* @since 10/27/2017 
* @version 1.0 
*/
class F {
    private Integer i;


    public F(){}
    public F(Integer i){
        this.i = i;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }
}

class C extends F {
    private String s;

    public C(){}
    public C(Integer i, String s){
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

class X{
    private Integer i;
    private String s;
    private C c;
    private Integer n;
    private int m;
    private Map<String,String> mp;
    private byte[] b;
    private BaseEntity entity;

    public X(){}
    X(Integer i, String s, C c, Integer n){
        this.i = i;
        this.s = s;
        this.c = c;
        this.n = n;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public C getC() {
        return c;
    }

    public void setC(C c) {
        this.c = c;
    }

    public Integer getN() {
        return n;
    }

    public void setN(Integer n) {
        this.n = n;
    }

    public int getM() {
        return m;
    }

    public void setM(int m) {
        this.m = m;
    }

    public Map<String, String> getMp() {
        return mp;
    }

    public void setMp(Map<String, String> mp) {
        this.mp = mp;
    }

    public byte[] getB() {
        return b;
    }

    public void setB(byte[] b) {
        this.b = b;
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }
}

class Y{
    private Integer i;
    private String s;
    private F c;
    private Long n;
    private long m;
    private Map<String,Object> mp;
    private int[] b;

    private BaseEntity entity;

    public Y(){}
    public Y(Integer i,String s,F c,Long n){
        this.i = i;
        this.s = s;
        this.c = c;
        this.n = n;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public F getC() {
        return c;
    }

    public void setC(F c) {
        this.c = c;
    }

    public Long getN() {
        return n;
    }

    public void setN(Long n) {
        this.n = n;
    }

    public long getM() {
        return m;
    }

    public void setM(long m) {
        this.m = m;
    }

    public Map<String, Object> getMp() {
        return mp;
    }

    public void setMp(Map<String, Object> mp) {
        this.mp = mp;
    }

    public int[] getB() {
        return b;
    }

    public void setB(int[] b) {
        this.b = b;
    }

    public BaseEntity getEntity() {
        return entity;
    }

    public void setEntity(BaseEntity entity) {
        this.entity = entity;
    }
}

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@SpringBootConfiguration //only enable when target module hasn't @SpringBootApplication
//@ComponentScan(basePackages = {"com.maoding"}) //only enable when target module hasn't @SpringBootApplication
public class BeanUtilsTest {

    /** for method: createFrom(final Object input, final Class<D> outputClass) */
    @Test
    public void testCreateFrom() throws Exception {
        long t = System.currentTimeMillis();

        for (int i=0; i<1000; i++) {
            X x = new X(1, "sss", new C(2, "ccc"), 5);
            x.setM(3);
            Map<String, String> mp = new HashMap<>();
            mp.put("aaa", "aaa");
            x.setMp(mp);
            byte[] b = new byte[1];
            b[0] = 7;
            x.setB(b);
            BaseEntity entity = new BaseEntity();
            entity.setLastModifyUserId("abcde");
            x.setEntity(entity);

            Y y = BeanUtils.createFromNew(x, Y.class);
//        BeanUtils.copyProperties(x,y);
            assert y != null;
            assertEquals((Integer) 1, y.getI());
            assertEquals("sss", y.getS());
            assertEquals(new Long(5), y.getN());
            assertEquals((Integer) 2, y.getC().getI());
            assertEquals(3, y.getM());
            assertEquals("aaa", y.getMp().get("aaa").toString());
            assertEquals(7, y.getB()[0]);
            assertEquals(entity.getId(), y.getEntity().getId());
            assertEquals(entity.getLastModifyTime(), y.getEntity().getLastModifyTime());
            assertEquals(entity.getLastModifyUserId(), y.getEntity().getLastModifyUserId());
        }
        System.out.println("===>testCreateFrom:" + (System.currentTimeMillis()-t) + "ms");
    }
    /** for method: copyProperties(final Map<String, Object> input, final Object output) */ 
    @Test
    public void testCopyPropertiesForInputOutput() throws Exception { 
        Map<String,Object> m = new HashMap<>();
        m.put("i",(Integer)1);
        m.put("s","sss");
        m.put("n", 5);
        m.put("m",5);
        m.put("c",new C(2,"ccc"));
        X x = new X();
        BeanUtils.copyPropertiesNew(m,x);
        assertEquals((Integer)1,x.getI());
        assertEquals("sss",x.getS());
        assertEquals((Integer)5,x.getN());
        assertEquals((Integer)2,x.getC().getI());
        assertEquals("ccc",x.getC().getS());
        Y y = new Y();
        BeanUtils.copyPropertiesNew(m,y);
        assertEquals((Integer)1,y.getI());
        assertEquals("sss",y.getS());
        assertEquals(new Long(5),y.getN());
        assertEquals((Integer)2,y.getC().getI());
    }

    /** for method: getProperty(final Object obj, final String ptyName) */ 
    @Test
    public void testGetProperty() throws Exception { 
        X x = new X(1,"sss",new C(2,"ccc"),5);
        assertEquals((Integer)1,BeanUtils.getProperty(x,"i"));
        assertEquals("sss",BeanUtils.getProperty(x,"s"));
        assertEquals(5,BeanUtils.getProperty(x,"n"));
        assertEquals((Integer)2,((C)BeanUtils.getProperty(x,"c")).getI());
        assertEquals("ccc",((C)BeanUtils.getProperty(x,"c")).getS());
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
