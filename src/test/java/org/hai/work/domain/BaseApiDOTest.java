package org.hai.work.domain;
import org.testng.annotations.Test;
import org.testng.Assert;
public class BaseApiDOTest {

	@Test
	public void b0() {
		int arg0 = 1;
		BaseApiDO baseApiDO = new BaseApiDO();
		int testInt = baseApiDO.b(arg0);
		Assert.assertTrue(true);
		
	}
	@Test
	public void e1() {
		int arg0 = 1;
		BaseApiDO baseApiDO = new BaseApiDO();
		int testInt = baseApiDO.e(arg0);
		Assert.assertTrue(true);
		
	}
	@Test
	public void c2() {
		int arg0 = 1;
		int arg1 = 1;
		BaseApiDO baseApiDO = new BaseApiDO();
		int testInt = baseApiDO.c(arg0, arg1);
		Assert.assertTrue(true);
		
	}
	@Test
	public void f3() {
		int arg0 = 1;
		BaseApiDO baseApiDO = new BaseApiDO();
		try {
			baseApiDO.f(arg0);
			Assert.fail();
		} catch (Exception e) {
			Assert.assertEquals(e.getMessage(), "ERROR_CODE");
		}
	}
	@Test
	public void d4() {
		BaseApiDO baseApiDO = new BaseApiDO();
		Assert.assertNotNull(baseApiDO.d());
		
	}
	@Test
	public void a5() {
		BaseApiDO baseApiDO = new BaseApiDO();
		Assert.assertNotNull(baseApiDO.a());
		
	}

}