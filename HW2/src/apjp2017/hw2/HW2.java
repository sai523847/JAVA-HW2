//******* DO NOT CHANGE FOLLOWING LINE!! ***********//
package apjp2017.hw2;
//***************************************************//

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.MethodAccessor_Character;

public class HW2 {

	/**
	 * <h3>TO-DO Problem:</h3> Given a Class instance c, find all interfaces which
	 * are directly or indirectly implemented/extended by c. For instance, since
	 * ArrayList implements List and List extends/implements Collection, as a result
	 * Collection.class should be in the result of getAllInstances(ArrayList.class).
	 * 
	 * Return the result as a set of Class instances so that we can test this method
	 * easily.
	 * 
	 * @param c
	 *            is a Class instance representing a class/interface
	 * @return the set of all interfaces implemented/extended by c.
	 */
	public static Set<Class<?>> getAllInterfaces(Class<?> c) {
		// TO-DO: put your code here!
		Set<Class<?>> rlt = new HashSet<>();
		Class<?>[] interfaces = c.getInterfaces();
		Class<?> superclass = c.getSuperclass();
		for(Class<?> inf : interfaces) {	
			rlt.add(inf);
		}
		if(superclass != null) {		
			getAllInterfaces(superclass);
		}
		return rlt;
	}

	/**
	 * <h3>TO-DO Problem:</h3> Given a Class instance c, find all methods m declared
	 * in the class represented by c the visibility of m is package (i.e., not
	 * public, protected nor private).
	 * 
	 * Return the result as a set of Class instances.
	 * 
	 * @param c
	 *            a Class instance to be inspected
	 * @return the set of all member methods whose visibility is at 'package' level.
	 */
	public static Set<Method> findAllPackageMethods(Class<?> c) {
		// To-DO: put your code here! 
		int modifiersNum = 0;
		Set<Method> rlt = new HashSet<Method>();
		for(Method method : c.getDeclaredMethods()) {
			modifiersNum = method.getModifiers();
			if(Modifier.isPublic(modifiersNum) != true & Modifier.isProtected(modifiersNum) != true & Modifier.isPrivate(modifiersNum) != true) {
				rlt.add(method);
			}
		}
		return rlt;
	}

	/**
	 * <h3>TO-DO Problem:</h3> A (static or non-static) method is called primitive
	 * if all its formal parameters are of primitive types and the type of return
	 * value is also a primitive type. For instance, the length() method of String
	 * has no input and return type is int. Hence it is a primitive method.
	 * 
	 * This problem requires you to find all primitive methods declared in a
	 * class/interface represented by the input Class instance c.
	 * 
	 * Return the result as a set.
	 * 
	 * @param c
	 *            a Class instance to be inspected
	 * @return the set of all primitive methods declared in the class/interface
	 *         represented by c.
	 */
	public static Set<Method> findAllPrimitiveMethods(Class<?> c) {
		// TO-DO: put your code here!
		Set<Method> rlt = new HashSet<Method>();
		for(Method method : c.getDeclaredMethods()) {
			if(method.getReturnType().isPrimitive() == true) {
				rlt.add(method);
			}
		}
		return rlt;
	}

	/**
	 * <h3>TO-DO Problem:</h3> A method is called numeric if its has a numeric
	 * output and all its input are primitive numeric types. Given an object obj(or
	 * null in case of static method) and a static/non-static numeric method m of
	 * any arity, return the value of obj.m(zero1,zero2,...), where zero1,... are 0
	 * or 0.0 depending on the type of parameters. In the case that m has variable
	 * arguments, just return obj.m(zero1). return the result as a double value.
	 * 
	 * So the result of defautlValueOfNumericMethod(Math.class.getMethod("cos",
	 * double.class), null) should be equlas 1.0.
	 * 
	 * @param m
	 *            a Method
	 * @param obj
	 *            an object whose class contains method m.
	 * @return the result of obj.m(0,...0) as a double value
	 */
	public static double defaultValueOfNumericMethod(Method m, Object obj) {
		// In order to access methods normal java access does not allow,
		// we need set the accessiblity flag of the method/field/constructor to true.
		m.setAccessible(true);
		// TO-DO: put your code here!
		double rlt = 0;
		try {
			rlt = (double)m.invoke(m.getDeclaringClass(), obj);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return rlt;
	}

	/**
	 * <h3>TO-DO Problem:</h3> Given two object obj and nobj, and a NON-STATIC Field
	 * f which is defined in both objects (i.e., obj.f and nobj.f are defined).
	 * Using reflection to copy the obj.f to nobj.f (ie., nobj.f = obj.f) using
	 * reflection.
	 * 
	 * @param f
	 *            A field which is not static.
	 * @param obj
	 *            an object in whcih f is defined.
	 * @param nobj
	 *            an obj in which f is defined.
	 * @throws Exception
	 *             if the operation cannot be performed.
	 */
	public static void copyNonStaticField(Field f, Object obj, Object nobj) throws Exception {

		if (Modifier.isStatic(f.getModifiers()))
			return;

		f.setAccessible(true); // in order to access normally inaccessible field, we need set this flag.

		// TO-DO: put your code here!
		Field[] objectField = obj.getClass().getDeclaredFields();
		Field[] nobjectField = nobj.getClass().getDeclaredFields();
		Field temp = null;
		for(Field of:objectField) {
			if(of.getName().equals(f.getName())) {
				temp = of;
			}
		}
		for(Field nof:nobjectField) {
			if(nof.getName().equals(temp.getName())) {
				nobj.getClass().getField(temp.getName()).set(nobj, temp.getInt(obj));
				System.out.println("後來A：" + obj.getClass().getField(temp.getName()).getInt(obj));
				System.out.println("後來B：" + nobj.getClass().getField(temp.getName()).getInt(nobj));
			}
		}
		return ;
	}

	/**
	 * <h3>NON-PROBLEM:</h3> Given a Class instance c representing a class, find all super classes
	 * (excluding interfaces) or itself. Return the result as a List. You may need
	 * this method for the reflectiveCopy() problem.
	 * 
	 * @param c
	 * @return
	 */
	private static List<Class<?>> getClasses(Class<?> c) {

		if (c == null)
			return new ArrayList<>();

		List<Class<?>> rlt = getClasses(c.getSuperclass());

		rlt.add(c);
		return rlt;

	}

	/**
	 * <h3>TO-DO Problem:</h3> Given an object obj of any type which is neither null
	 * nor an array, construct a new object nobj of the same class such that obj and
	 * nobj have same value on all fields including all non-pubic fields in all
	 * ancestor classes. Note you cannot simply return obj (i.e., nobj != obj).<br/>
	 * Hint: <br/>
	 * 1. Find some constructor ctr from obj and call ctr.newInstance(...) with approprivate arguments. <br/>
	 * 2. Find all non-static fields f (including those from super classes), copy f value from obj to the new object.  
	 * 
	 * Issue: How to test this method?
	 * 
	 * @param obj
	 *            an object which is neither null nor an array.
	 * @return a duplicate of obj
	 */
	public static Object reflectiveCopy(Object obj) throws Exception {
		//TO-DO: Put your code here!
		Object rlt = obj.getClass().newInstance();
		Field[] objf = obj.getClass().getDeclaredFields();
		Field[] rltf = rlt.getClass().getDeclaredFields();
		List<Field> field = new ArrayList<>();
		for(Field f:objf)
		{
			f.setAccessible(true);
			field.add(f);
		}
		for(Field f:field) {
			if(Modifier.isStatic(f.getModifiers())) {
				continue;
			} else {
				for(int i = 0; i < rltf.length ; i++) {
					if(f.getName().equals(rltf[i].getName())) {
						try {
							rltf[i].setAccessible(true);
							rltf[i].set(rlt, f.get(obj));
						}catch(Exception e) {
							
						}
					}
				}
			}
		}
		List<Class<?>> list = getClasses(rlt.getClass());
		for(Class<?>l:list){
			if(l.toString().equals("class java.lang.Object")) {
				return rlt;
			}
			reflectiveCopy(l);
		}
		return rlt;
	}

	/**
	 * Class A and B are put here used only for the purpose of testing
	 * reflectiveCopy().
	 * 
	 * @author chencc
	 *
	 */

	public static class A {
		private int x;
		public int y;

		public void setX(int x1) {
			x = x1;
		}

		public String toString() {
			return String.format("A(x=%s,y=%s)", x, y);
		}

	}

	public static class B extends A {
		public int y;
		// public final w ;
		private int z;

		public B() {
		}

		public B(int ax, int ay, int by, int bz) {
			y = by;
			z = bz;
			setX(ax);
			super.y = ay;
		}

		public static B getB1() {
			return new B(1, 2, 3, 4);
		}

		public String toString() {
			return super.toString() + "<--" + String.format("B(y=%s,z=%s)", y, z);
		}

	}

	public static void testReflectiveCopy() throws Exception {

		B b1 = B.getB1();
		B nb1 = (B) reflectiveCopy(b1);

		System.out.println(" b1 is " + b1);
		System.out.println("nb1 is " + nb1);

		List<String> ss = new ArrayList<>();
		ss.add("abc");
		ss.add("def");

		Object nss = reflectiveCopy(ss);
		System.out.println(" ss is " + ss);
		System.out.println("nss is " + nss);
		System.out.println("ss.equals(nss)?" + (ss.equals(nss)));

		Map<String, Number> mp1 = new HashMap<>();
		mp1.put("Chen", 20);
		mp1.put("Wang", 50);

		Object nmp1 = reflectiveCopy(mp1);
		System.out.println(" mp1 is " + mp1);
		System.out.println("nmp1 is " + nmp1);
		System.out.println("mp1.equals(mp1)?" + (mp1.equals(nmp1)));

	}
	
	
	public static void main(String[] args) throws Exception {
		// Write your test code for other problems here!
		//test getAllInterfaces()
		System.out.println("*************** test getAllInterfaces() **************************");
		String o = "" ;
		Class<?> c = o.getClass();
		Set<Class<?>> result = new HashSet<>();
		result = getAllInterfaces(c);
		System.out.println(result);
		System.out.println("");
		
		//test findAllPackageMethods()
		System.out.println("*************** test findAllPackageMethods() *********************");
		Set<Method> MethodsRlt = new HashSet<>();
		MethodsRlt = findAllPackageMethods(c);
		System.out.println(MethodsRlt);
		System.out.println("");
		
		//test findAllPrimitiveMethods()
		System.out.println("*************** test findAllPrimitiveMethods() *******************");
		Set<Method> PrimitiveMethodsRlt = new HashSet<>();
		PrimitiveMethodsRlt = findAllPrimitiveMethods(c);
		System.out.println(PrimitiveMethodsRlt);
		System.out.println("");
		
		//test defaultValueOfNumericMethod()
		System.out.println("*************** test defaultValueOfNumericMethod() ***************");
		System.out.println(defaultValueOfNumericMethod(Math.class.getMethod("cos",double.class), Integer.valueOf(0)));
		System.out.println("");
		
		//test copyNonStaticField()
		System.out.println("*************** test copyNonStaticField()*************************");
		A a = new A(); 
		B b = new B();
		a.y = 10;
		b.y = 5;
		System.out.println("原本A：" + a.y);
		System.out.println("原本B：" + b.y);
		copyNonStaticField(a.getClass().getDeclaredFields()[1],a,b);
		System.out.println("");
		
		//test testReflectiveCopy()
		System.out.println("*************** test testReflectiveCopy() ***********************");
		testReflectiveCopy();
		
	}

}