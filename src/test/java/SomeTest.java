import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.aeroplanechess.builder.GameBuilder;
import com.aeroplanechess.config.AppConfig;
import com.aeroplanechess.utils.DiceUtils;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class SomeTest {

	@Autowired
	GameBuilder gameBuilder;

	@Autowired
	DiceUtils diceUtils;

	@Test
	public void buildGame() {
		System.out.println(gameBuilder.build().toString());
	}

}
