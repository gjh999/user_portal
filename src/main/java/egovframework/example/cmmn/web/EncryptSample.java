package egovframework.example.cmmn.web;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import go.mdds.sdk.util.CipherUtil;

/**
 * @author GOOJAEHO
 *
 */
public class EncryptSample {

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() throws Exception {
		//fail("Not yet implemented");
		System.out.println("테스트 시작");

		String orgData = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
				"<Envelope>\r\n" +
				"	<Header>\r\n" +
				"		<commonHeader>\r\n" +
				"			<apiKeyHashCode>0c62599e-d2f3-4ac1-9839-39ad909d94a8</apiKeyHashCode>\r\n" +
				"			<useSystemCode>1741000000000013</useSystemCode>\r\n" +
				"			<serviceId>MDS0000335</serviceId>\r\n" +
				"			<transactionUniqueId>2020081118480654712345678</transactionUniqueId>\r\n" +
				"			<agreementYn>Y</agreementYn>\r\n" +
				"			<timestampToken>#GTSA_Time_Stamp_Token</timestampToken>\r\n" +
				"		</commonHeader>\r\n" +
				"	</Header>\r\n" +
				"	<Body>\r\n" +
				"		<request>\r\n" +
				"			<userInfo>\r\n" +
				"				<userCid>C99393949043939</userCid>\r\n" +
				"				<userName>홍테스</userName>\r\n" +
				"				<userRrn>1234561234567</userRrn>\r\n" +
				"			</userInfo>\r\n" +
				"		</request>\r\n" +
				"	</Body>\r\n" +
				"</Envelope>\r\n" +
				"";

		String apiKey = "0c62599e-d2f3-4ac1-9839-39ad909d94a8";
		String apiUtlinsttCode = "1741000000000013";

		// 암호화 할떄 ( orgData , apiKey, apiUtlinsttCode )
		String encData = CipherUtil.encryptAria(orgData, apiKey, apiUtlinsttCode);
		System.out.println("encData = " + encData);

		// 복호화 할때 ( endData , apiKey, apiUtlinsttCode )
		String decData = CipherUtil.decryptAria(encData, apiKey, apiUtlinsttCode);
		System.out.println("decData = " + decData);

		System.out.println("테스트 끝");

	}

}
