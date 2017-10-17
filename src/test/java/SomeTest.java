import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.dotterbear.aeroplanechess.config.AppConfig;
import com.dotterbear.aeroplanechess.utils.GameUtils;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { AppConfig.class })
public class SomeTest {

	@Autowired
	GameUtils gameUtils;

	@Value(value = "${websocket.aeroplanechess.config.dice.min}")
	int diceMin;

	@Value(value = "${websocket.aeroplanechess.config.dice.max}")
	int diceMax;

	@Test
	public void roll() {
		int roll;
		for (int i = 0; i < 100; i++) {
			roll = gameUtils.roll();
			assertTrue(roll >= diceMin && roll <= diceMax);
		}
	}

}
