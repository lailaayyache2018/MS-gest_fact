package org.sid.iventoryservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;
import org.springframework.web.bind.annotation.CrossOrigin;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@SpringBootApplication
public class IventoryServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(IventoryServiceApplication.class, args);
	}
	@Bean
	CommandLineRunner start(ProductRepository productRepository, RepositoryRestConfiguration restConfiguration){
		restConfiguration.exposeIdsFor(Product.class);
		//--------------------------------
		restConfiguration.getCorsRegistry()
				.addMapping("/**")

				.allowedOrigins("*")
				.allowedHeaders("*")
				.allowedMethods("OPTIONS","HEAD","GET","PUT","DELETE","PATCH");
		//-------------------------------------------------------------
		return args -> {
			productRepository.save(new Product(null,"ordinateur",788,12));
			productRepository.save(new Product(null,"Imprimante",20,17));
			productRepository.save(new Product(null,"smartphone",30,19));
			productRepository.findAll().forEach(p ->{
				System.out.println(p.getName());
			} );


		};
	}



}
@Entity
@Data @NoArgsConstructor @AllArgsConstructor @ToString
class Product{
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private double prix;
	private double quantity;
}
@CrossOrigin("*")
@RepositoryRestResource
interface ProductRepository extends JpaRepository<Product,Long>{

}

