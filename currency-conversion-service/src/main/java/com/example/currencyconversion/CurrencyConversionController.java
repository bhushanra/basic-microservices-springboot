package com.example.currencyconversion;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;  
import org.springframework.web.bind.annotation.PathVariable;  
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;  
@RestController  
public class CurrencyConversionController   
{
	
	
	@Autowired
	private CurrencyExchangeServiceProxy proxy;  

	@Autowired
	private RestTemplate restTemplate;
	
	@GetMapping("/currency-converter/from/{from}/to/{to}/quantity/{quantity}") //where {from} and {to} represents the column   
	public CurrencyConversionBean convertCurrency(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity)  
	{  
	//return new CurrencyConversionBean(1L, from,to,BigDecimal.ONE, quantity,quantity,0 );  
		Map<String, String>uriVariables=new HashMap<>();  
		uriVariables.put("from", from);  
		uriVariables.put("to", to);
		//calling the currency-exchange-service  
		//ResponseEntity<CurrencyConversionBean>responseEntity=new RestTemplate().getForEntity("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);  
		
		//CurrencyConversionBean response=new RestTemplate().getForObject("http://localhost:8000/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);
		CurrencyConversionBean response=restTemplate.getForObject("http://CURRENCY-EXCHANGE-SERVICE/currency-exchange/from/{from}/to/{to}", CurrencyConversionBean.class, uriVariables);

		
		//CurrencyConversionBean response=responseEntity.getBody();  
		//creating a new response bean and getting the response back and taking it into Bean  
		return new CurrencyConversionBean(response.getId(), from,to,response.getConversionMultiple(), quantity,quantity.multiply(response.getConversionMultiple()),response.getPort());  
	}


	//mapping for currency-converter-feign service
	@GetMapping("/currency-converter-feign/from/{from}/to/{to}/quantity/{quantity}") //where {from} and {to} represents the column 
	//returns a bean 
	public CurrencyConversionBean convertCurrencyFeign(@PathVariable String from, @PathVariable String to, @PathVariable BigDecimal quantity)
	{
		CurrencyConversionBean response=proxy.retrieveExchangeValue(from, to);
	//creating a new response bean
	//getting the response back and taking it into Bean
	return new CurrencyConversionBean(response.getId(), from, to, response.getConversionMultiple(), quantity, quantity.multiply(response.getConversionMultiple()), response.getPort());
	}


}  
