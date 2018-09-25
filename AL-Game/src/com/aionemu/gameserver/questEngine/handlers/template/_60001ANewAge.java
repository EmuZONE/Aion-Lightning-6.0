/**
 * This file is part of Aion-Lightning <aion-lightning.org>.
 *
 *  Aion-Lightning is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  Aion-Lightning is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details. *
 *  You should have received a copy of the GNU General Public License
 *  along with Aion-Lightning.
 *  If not, see <http://www.gnu.org/licenses/>.
 */
package quest.poeta;

import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Falke_34
 */
public class _60001ANewAge extends QuestHandler {

	private final static int questId = 60001;

	public _60001ANewAge() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(790001).addOnTalkEvent(questId); // Pernos
		qe.registerQuestNpc(820134).addOnTalkEvent(questId); // Guide to rare poison
		qe.registerQuestNpc(820135).addOnTalkEvent(questId); // Day of the Storm
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
	}

	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env) {
		return defaultOnZoneMissionEndEvent(env);
	}

	@Override
	public boolean onLvlUpEvent(QuestEnv env) {
		return defaultOnLvlUpEvent(env, 60000, true);
	}	

	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("AKARIOS_VILLAGE_210010000"));
	}	
	
	@Override
	public boolean onDialogEvent(QuestEnv env) {
		Player player = env.getPlayer();
		QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
			return false;		
		DialogAction dialog = env.getDialog();
		int targetId = env.getTargetId();		

		
		if (qs.getStatus() == QuestStatus.START) {
			if (targetId == 790001) {
				switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1011);
				case SETPRO1:
					return defaultCloseDialog(env, 0, 1);
				default:
					break;
				}
			} else if (targetId == 820134) {
				switch (dialog) {
				case QUEST_SELECT:
					playQuestMovie(env, 3);
					return sendQuestDialog(env, 1353);
				case SETPRO2:
					return defaultCloseDialog(env, 1, 2);
				default:
					break;
				}
			} else if (targetId == 820135) {
				switch (dialog) {
				case QUEST_SELECT:
					return sendQuestDialog(env, 1694);
				case SET_SUCCEED:
					qs.setQuestVar(3); // 3
					changeQuestStep(env, 3, 3, true); // reward
					updateQuestStatus(env);
					return closeDialogWindow(env);
				default:
					break;
				}
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			if (targetId == 790001) { // Pernos
				if (env.getDialog() == DialogAction.USE_OBJECT) {
					return sendQuestDialog(env, 10002);
				}
				else {
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}
