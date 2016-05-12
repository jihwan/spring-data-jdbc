package util;
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.ConfigurableApplicationContext;

public class BeanDefinitionUtils  {
	
	private BeanDefinitionUtils() {}
	
	public static void printBeanDefinitions(ConfigurableApplicationContext context) {
		
		List<List<String>> roleBeanInfos = new ArrayList<List<String>>();
		roleBeanInfos.add(new ArrayList<String>());
		roleBeanInfos.add(new ArrayList<String>());
		roleBeanInfos.add(new ArrayList<String>());
		
		for(String name : context.getBeanDefinitionNames()) {
			
			int role = context.getBeanFactory().getBeanDefinition(name).getRole();
			List<String> beanInfos = roleBeanInfos.get(role);
			
			beanInfos.add(role + " : " + name + " : " + context.getBean(name).getClass().getName());
		}
		
		for(List<String> beanInfos : roleBeanInfos) {
			for(String beanInfo : beanInfos) {
				System.out.println(beanInfo);
			}
		}
		
		System.out.println(">>>>>>>>>>>>>>>>>>>>>>> " + context.getBeanDefinitionNames().length + " spring beans loaded.");
	}
}