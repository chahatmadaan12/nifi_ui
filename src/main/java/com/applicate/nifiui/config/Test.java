package com.applicate.nifiui.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.applicate.nifiui.service.NiFiSchemaServices;
@Component
public class Test {
	
	@Autowired
	public NiFiSchemaServices schemaServices;
	
	enum Type{
		ABC{

			@Override
			public String getType() {
				// TODO Auto-generated method stub
				return "ABC";
			}
			
		},
		CDE{

			@Override
			public String getType() {
				// TODO Auto-generated method stub
				return "CDE";
			}
			
		},
		Default{

			@Override
			public String getType() {
				// TODO Auto-generated method stub
				return "Default";
			}
			
		};
		
		public static Type hasEnum(String value){
			value=value.toUpperCase();
			for(Type type:Type.values()) {
				if(type.toString().equals(value)) {
					return type;
				}
			}
			return Type.Default;
		}
		
		public abstract String getType();
	}
	
	public static void main(String[] args) {
		System.out.println(Type.hasEnum("abc").getType());
	}

}
