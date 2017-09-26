package org.kerrin.dungeon.task;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.kerrin.dungeon.enums.HiscoreType;
import org.kerrin.dungeon.model.Achievement;
import org.kerrin.dungeon.model.Hiscore;
import org.kerrin.dungeon.service.AccountMessageService;
import org.kerrin.dungeon.service.AchievementService;
import org.kerrin.dungeon.service.HiscoreService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

/**
 * Reorder the high scores periodically
 * 
 * @author Kerrin
 *
 */
@Configurable
public class ScoreProcessorTask {
	private static final Logger logger = LoggerFactory.getLogger(ScoreProcessorTask.class);

	@Autowired
	private HiscoreService hiscoreService;
	
	@Autowired
	private AchievementService achievementService;
	
	@Autowired
	private AccountMessageService accountMessageService;
		
	/**
	 * Every 5 minutes, set the highscore order for the top 1000 people in the past month
	 */
	@Transactional
	@Scheduled(fixedRate=300000)
	protected void run() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -28);
		Date sinceDate = cal.getTime();
		boolean hardcore = false;
		boolean ironborn = false;
		/* This loop should try all 4 permutations of hardcore and ironborn
		 Order will be: hardcore,ironborn
		 				false,false
						true,false
						false,true
						true,true
		 */
		for(int i=0; i<4; i++) {
			hiscoreService.unrankOutdatedScores(hardcore, ironborn, sinceDate);
			for(HiscoreType type: HiscoreType.values()) {
				List<Hiscore> orderedScores = hiscoreService.findAllByTypeAndHardcoreAndIronborn(
						type, hardcore, ironborn, sinceDate, 0, 1000);
				int position = 1;
				for(Hiscore score:orderedScores) {
					score.setRank(position++);
					hiscoreService.update(score);
//					StringBuilder message = new StringBuilder("New ");
//					message.append(score.getType().getNiceName());
//					message.append(" Hiscore Rank for ");
//					if(score.getHardcore()) {
//						if(score.getIronborn()) {
//							message.append("Extream");
//						} else {
//							message.append("Hardcore");
//						}
//					} else {
//						if(score.getIronborn()) {
//							message.append("Ironborn");
//						} else {
//							message.append("Normal");
//						}
//					}
//					message.append(" mode: ");
//					message.append(score.getRank());
//					accountMessageService.create(score.getAccount().getId(), message.toString());
					List<Achievement> newAchievements = achievementService.hiscore(null, score);
					int newPoints = 0;
					for(Achievement achievement:newAchievements) {
						newPoints += achievement.getPoints();
					}
					if(newPoints > 0) {
						hiscoreService.achievement(score.getAccount(), newPoints);
					}
				}
			}
			// Toggle the booleans
			if(hardcore) ironborn = !ironborn;
			hardcore = !hardcore;
		}

	}
}
