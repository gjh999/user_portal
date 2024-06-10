/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.example.sample.service.impl;

import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import egovframework.example.cmmn.web.ConvertUtil;
import egovframework.example.sample.service.EgovSampleService;
import egovframework.example.sample.service.SampleDefaultVO;
import egovframework.example.sample.service.SampleVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.fdl.idgnr.EgovIdGnrService;
import go.mdds.sdk.util.CipherUtil;

/**
 * @Class Name : EgovSampleServiceImpl.java
 * @Description : Sample Business Implement Class
 * @Modification Information
 * @
 * @  수정일      수정자              수정내용
 * @ ---------   ---------   -------------------------------
 * @ 2009.03.16           최초생성
 *
 * @author 개발프레임웍크 실행환경 개발팀
 * @since 2009. 03.16
 * @version 1.0
 * @see
 *
 *  Copyright (C) by MOPAS All right reserved.
 */

@Service("sampleService")
public class EgovSampleServiceImpl extends EgovAbstractServiceImpl implements EgovSampleService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSampleServiceImpl.class);

	/** SampleDAO */
	// TODO ibatis 사용
	@Resource(name = "sampleDAO")
	private SampleDAO sampleDAO;
	// TODO mybatis 사용
	//  @Resource(name="sampleMapper")
	//	private SampleMapper sampleDAO;

	/** ID Generation */
	@Resource(name = "egovIdGnrService")
	private EgovIdGnrService egovIdGnrService;

	/**
	 * 글을 등록한다.
	 * @param vo - 등록할 정보가 담긴 SampleVO
	 * @return 등록 결과
	 * @exception Exception
	 */
	@Override
	public String insertSample(SampleVO vo) throws Exception {
		LOGGER.debug(vo.toString());

		/** ID Generation Service */
		String id = egovIdGnrService.getNextStringId();
		vo.setId(id);
		LOGGER.debug(vo.toString());

		sampleDAO.insertSample(vo);
		return id;
	}

	/**
	 * 글을 수정한다.
	 * @param vo - 수정할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void updateSample(SampleVO vo) throws Exception {
		sampleDAO.updateSample(vo);
	}

	/**
	 * 글을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 SampleVO
	 * @return void형
	 * @exception Exception
	 */
	@Override
	public void deleteSample(SampleVO vo) throws Exception {
		sampleDAO.deleteSample(vo);
	}

	/**
	 * 글을 조회한다.
	 * @param vo - 조회할 정보가 담긴 SampleVO
	 * @return 조회한 글
	 * @exception Exception
	 */
	@Override
	public SampleVO selectSample(SampleVO vo) throws Exception {
		SampleVO resultVO = sampleDAO.selectSample(vo);
		if (resultVO == null)
			throw processException("info.nodata.msg");
		return resultVO;
	}

	/**
	 * 글 목록을 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 목록
	 * @exception Exception
	 */
	@Override
	public List<?> selectSampleList(SampleDefaultVO searchVO) throws Exception {
		return sampleDAO.selectSampleList(searchVO);
	}

	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	@Override
	public int selectSampleListTotCnt(SampleDefaultVO searchVO) {
		return sampleDAO.selectSampleListTotCnt(searchVO);
	}
	
	/**
	 * 글 총 갯수를 조회한다.
	 * @param searchVO - 조회할 정보가 담긴 VO
	 * @return 글 총 갯수
	 * @exception
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectResponseIssue(String encData, String apiKey, String insttId) throws Exception {
		
		// 복호화 할때 ( endData , apiKey, apiUtlinsttCode )
		//String decData = CipherUtil.decryptAria(encData, apiKey, apiUtlinsttCode);
		//System.out.println("decData = " + decData);
		
		String decData 		= null;

    	try {
    		decData = CipherUtil.decryptAria(encData, apiKey, insttId);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.debug("NoSuchAlgorithmException Occured");
		} catch (UnsupportedEncodingException e) {
			LOGGER.debug("UnsupportedEncodingException Occured");
		}

    	if(decData == null) {
    		decData = "";
    	}

    	// 사용자  정보 추출
		JSONObject userObj = new JSONObject();

    	// xml 공통 해더 정보 추출
    	JSONObject xmlHeaderObj = new JSONObject();

		// xml 해더정보
		@SuppressWarnings("unused")
		String xmlHeader = "";
    	
    	Document doc = null;
    	try(StringReader sr = new StringReader(decData);){
    		InputSource is = new InputSource(sr);
    		doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(is);
    	}

    	/* ======================================= Header 추출 ======================================= */
    	TransformerFactory tf = TransformerFactory.newInstance();
    	try {
    		tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
    		tf.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
    	} catch (IllegalArgumentException e) {
    		LOGGER.error("Not supported : IllegalArgumentException Occured");
    		LOGGER.info("Not supported : IllegalArgumentException Occured" + e);
    	}
        Transformer tr = tf.newTransformer();
        tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION,"yes");

        String xmlString = "";
        try(StringWriter swH = new StringWriter();){
        tr.transform(new DOMSource(doc), new StreamResult(swH));
        xmlString = swH.getBuffer().toString();
        }

        int xmlStartIdx = xmlString.indexOf("<Header>");
        int xmlEndIdx = xmlString.indexOf("</Header>")+9;
        xmlHeader = xmlString.substring(xmlStartIdx, xmlEndIdx);

        /* ====================== xml 공통 해더 정보 추출 =======================================*/
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList nodes = (NodeList)xpath.evaluate("//Header/commonHeader/*", doc, XPathConstants.NODESET);

        String nodeValue = "";
		String nodeKey = "";

        for (int loop = 0; loop < nodes.getLength(); loop++) {
			nodeKey = nodes.item(loop).getNodeName();
			nodeValue = nodes.item(loop).getTextContent();

			xmlHeaderObj.put(nodeKey, nodeValue);
		}

        /* ====================== Request Body 정보 추출 =======================================*/
		NodeList userInfo = doc.getElementsByTagName("userInfo");

		// API 서비스와 API Key 맵핑 체크
		HashMap<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("insttId", insttId);
		paramMap.put("apiKey", apiKey);

		// 권한 검증
		int cnt = 1;
		//cnt = sampleDAO.selectTotCntApiBundle(paramMap);

		if (cnt < 1) {
	        Map<String, Object> outputMap = new HashMap<String, Object>();
	    	outputMap.put("rspnsCode", 		"003");
	    	outputMap.put("rspnsMssage", 	"API 이용기관으로 등록되어 있지 않습니다.");
	        return outputMap;
		}

		// 사용자 검증 정보
		for (int i = 0; i < userInfo.getLength(); i++) {
			Node nNode = userInfo.item(i);
	    	if(nNode.getNodeType() == Node.ELEMENT_NODE) {
	    		Element element = (Element) nNode;
	    		for(int j=0; j<element.getChildNodes().getLength(); j++) {
	    			if(element.getChildNodes().item(j).getNodeType() == Node.ELEMENT_NODE) {
	    				userObj.put(element.getChildNodes().item(j).getNodeName(), element.getChildNodes().item(j).getTextContent());
	    			}
	    		}
	    	}
		}
		LOGGER.info("사용자 검증 정보 json object ============= : "+userObj);

		

    	/* ====================== 사용자 검증 =======================================*/
		String userCid = "";
    	if(ObjectUtils.isEmpty(userObj)) {
    		Map<String, Object> outputMap = new HashMap<String, Object>();
	    	outputMap.put("rspnsCode", 		"098");
	    	outputMap.put("rspnsMssage", 	"API 요청 필수값이 없습니다.");		// 사용자 검증 정보가 없습니다.														// 거래일련번호
	        return outputMap;
    	} else {
    		userCid = userObj.get("userCid").toString();
    		if(StringUtils.isEmpty(userCid)) {
    			Map<String, Object> outputMap = new HashMap<String, Object>();
    	    	outputMap.put("rspnsCode", 		"098");
    	    	outputMap.put("rspnsMssage", 	"API 요청 필수값이 없습니다.");		// 사용자 CI 값이 없습니다.
    	        return outputMap;
    		}
    	}

		/* ====================== 제3자 제공현황 조회 =======================================*/
    	Map<String, Object> outputMap = new HashMap<String, Object>();
    	SampleDefaultVO searchVO = new SampleDefaultVO();
		// 제3자 제공현황 조회
		List<?> resultList = sampleDAO.selectSampleList(searchVO);
		
		/* ====================== 더미 데이터 =======================================*/
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	
    	Map<String, Object >mapData1 = new HashMap<String,Object>();
    	mapData1.put("pvsnDt", 			"20240213");
    	mapData1.put("rcptnInstCdNm", 	"교육부");
    	mapData1.put("pvsnPrpsCtt",		"pvsnPrpsCtt01");
    	mapData1.put("pvsnInfoCtt", 	"pvsnInfoCtt01");
    	mapData1.put("pvsnBasis", 		"pvsnBasis01");
    	
    	list.add(mapData1);
    	
    	Map<String, Object >mapData2 = new HashMap<String,Object>();
    	mapData2.put("pvsnDt", 			"20240206");
    	mapData2.put("rcptnInstCdNm", 	"보건복지부");
    	mapData1.put("pvsnPrpsCtt",		"pvsnPrpsCtt01");
    	mapData1.put("pvsnInfoCtt", 	"pvsnInfoCtt01");
    	mapData1.put("pvsnBasis", 		"pvsnBasis01");
    	
    	list.add(mapData2);
    	
    	Map<String, Object >mapData3 = new HashMap<String,Object>();
    	mapData3.put("pvsnDt", 			"20230102");
    	mapData3.put("rcptnInstCdNm", 	"교육부");
    	mapData1.put("pvsnPrpsCtt",		"pvsnPrpsCtt01");
    	mapData1.put("pvsnInfoCtt", 	"pvsnInfoCtt01");
    	mapData1.put("pvsnBasis", 		"pvsnBasis01");
    	
    	list.add(mapData3);	
    	
    	String jsonData = ConvertUtil.convertListToJsonString((List<Map<String, Object>>) resultList);
		//String jsonData = ConvertUtil.convertListToJsonString((List<Map<String, Object>>) list);
		
		outputMap.put("rspnsCode", "100");
		outputMap.put("rspnsMssage", "정상 처리되었습니다.");
		outputMap.put("encData", CipherUtil.encryptAria(jsonData, apiKey, insttId));
		
		return outputMap;
			
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, Object> selectTestDataList(String testData) throws Exception {

		/* ====================== 제3자 제공현황 조회 =======================================*/
    	Map<String, Object> outputMap = new HashMap<String, Object>();
    	SampleDefaultVO searchVO = new SampleDefaultVO();
		// 제3자 제공현황 조회
		//List<?> resultList = sampleDAO.selectSampleList(searchVO);
		
		/* ====================== 더미 데이터 =======================================*/
    	List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
    	
    	Map<String, Object >mapData1 = new HashMap<String,Object>();
    	mapData1.put("pvsnDt", 			"20240213");
    	mapData1.put("rcptnInstCdNm", 	"교육부");
    	mapData1.put("pvsnPrpsCtt",		"정보주체 신청 및 실적정보 제공");
    	mapData1.put("pvsnInfoCtt", 	"봉사 실적 정보");
    	mapData1.put("pvsnBasis", 		"개인정보보호법 및 자원봉사활동 기본법");
    	
    	list.add(mapData1);
    	
    	Map<String, Object >mapData2 = new HashMap<String,Object>();
    	mapData2.put("pvsnDt", 			"20240206");
    	mapData2.put("rcptnInstCdNm", 	"보건복지부");
    	mapData2.put("pvsnPrpsCtt",		"정보주체 신청 및 실적정보 제공");
    	mapData2.put("pvsnInfoCtt", 	"봉사 실적 정보");
    	mapData2.put("pvsnBasis", 		"개인정보보호법 및 자원봉사활동 기본법");
    	
    	list.add(mapData2);
    	
    	Map<String, Object >mapData3 = new HashMap<String,Object>();
    	mapData3.put("pvsnDt", 			"20230102");
    	mapData3.put("rcptnInstCdNm", 	"교육부");
    	mapData3.put("pvsnPrpsCtt",		"정보주체 신청 및 실적정보 제공");
    	mapData3.put("pvsnInfoCtt", 	"봉사 실적 정보");
    	mapData3.put("pvsnBasis", 		"개인정보보호법 및 자원봉사활동 기본법");
    	
    	list.add(mapData3);	
    	
    	String jsonData = ConvertUtil.convertListToJsonString(list);
		//String jsonData = ConvertUtil.convertListToJsonString((List<Map<String, Object>>) list);
		
		outputMap.put("rspnsCode", "100");
		outputMap.put("rspnsMssage", "정상 처리되었습니다.");
		outputMap.put("result", jsonData);
		
		return outputMap;		
	}
}
