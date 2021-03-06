package monitoring.kafka;

import java.util.List;
import java.util.Properties;

import org.json.JSONArray;
import org.json.JSONObject;

import eu.supersede.integration.api.analysis.proxies.KafkaClient;
import kafka.javaapi.producer.Producer;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;
import monitoring.model.MonitoringData;

public class KafkaCommunication {

	
	Producer<String, String> producer;
	KafkaClient proxy;
	
	/**
	 * Creates a new instantiation of the IF kafka client proxy
	 */
	public void initProxy(String kafkaEndpoint) {
		proxy = new KafkaClient(kafkaEndpoint);
	}
	
	/**
	 * Creates a new producer for kafka communication
	 */
	public void initProducer(String kafkaEndpoint) {
		Properties props = new Properties();
		props.put("metadata.broker.list", kafkaEndpoint);
		props.put("serializer.class", "kafka.serializer.StringEncoder");
		props.put("request.required.acks", "1");
		ProducerConfig config = new ProducerConfig(props);
		this.producer = new Producer<String,String>(config);
	}
	
	/**
	 * Generates a json formatted response and sends it to the IF kafka consumer
	 */
	public void generateResponseIF(List<? extends MonitoringData> dataList, String timeStamp, 
			int outputId, int confId, String topic, String responseName) {
		JSONObject res = generateData(dataList, timeStamp, outputId, confId, responseName);
		proxy.sendMessage(res, topic);
	}
	
	/**
	 * Generates a json formatted response and sends it to a custom kafka consumer
	 */
	public void generateResponseKafka(List<? extends MonitoringData> dataList, String timeStamp, 
			int outputId, int confId, String topic, String responseName) {
		JSONObject res = generateData(dataList, timeStamp, outputId, confId, responseName);
		KeyedMessage<String, String> msg = new KeyedMessage<String, String>(topic, res.toString());
		producer.send(msg);
	}
	
	/**
	 * Generates a json formatted response and sends it to kafka
	 * @param dataList			a list with the data to retrieve
	 * @param timeStamp			the timestamp of the search
	 * @param producer			the producer to communicate 
	 * @param outputId			the id of the response data
	 * @param confId			the id of the configuration
	 * @param topic				the topic for the kafka communication
	 */
	public JSONObject generateData(List<? extends MonitoringData> dataList, String timeStamp, 
		int outputId, int confId, String responseName) {
		
		JSONArray dataItems = new JSONArray();
		
		for (MonitoringData dataItem : dataList) {
			JSONObject jsonDataItem = dataItem.toJsonObject();			
			dataItems.put(jsonDataItem);
		}
		
		JSONObject mainInfo = new JSONObject();
		
		mainInfo.put("idOutput", outputId);
		mainInfo.put("confId", confId);
		mainInfo.put("searchTimeStamp", timeStamp);
		mainInfo.put("numDataItems", dataList.size());
		mainInfo.put("DataItems", dataItems);
		
		++outputId;
		
		JSONObject jsonData = new JSONObject();
		
		jsonData.put(responseName, mainInfo);
		
		return jsonData;
		
	}	
	
}
