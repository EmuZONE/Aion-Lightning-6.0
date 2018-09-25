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

import com.aionemu.gameserver.model.DialogAction;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Falke_34
 */
public class _60000PernosCall extends QuestHandler {

	private final static int questId = 60000;

	public _60000PernosCall() {
		super(questId);
	}

	@Override
	public void register() {
		qe.registerQuestNpc(790001).addOnTalkEvent(questId); // Pernos
		qe.registerQuestNpc(820000).addOnTalkEvent(questId); // Old Friend Royer
		qe.registerOnEnterZone(ZoneName.get("AKARIOS_VILLAGE_210010000"), questId);
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
			switch (targetId) {
				case 820000: { // Old Friend Royer
					switch (dialog) {
					case QUEST_SELECT:
						return sendQuestDialog(env, 1011);
					case SET_SUCCEED:
						qs.setQuestVar(1); // 1
						changeQuestStep(env, 1, 1, true); // reward
						updateQuestStatus(env);
						return closeDialogWindow(env);
					default:
						break;
					}
				} 
			}
		} else if (qs.getStatus() == QuestStatus.REWARD) {
			switch (targetId) {
				case 790001: { // Pernos
					if (env.getDialogId() == DialogAction.SELECTED_QUEST_NOREWARD.id()) {
						int[] ids = { 60001, 60003, 60004, 60005, 60007, 1006, 60008, 60009, 60100 };
						for (int id : ids) {
							QuestEngine.getInstance().onEnterZoneMissionEnd(
								new QuestEnv(env.getVisibleObject(), env.getPlayer(), id, env.getDialogId()));
						}
					}
					if (env.getDialog() == DialogAction.USE_OBJECT) {
						return sendQuestDialog(env, 10002);
					}
					else {
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName) {
		return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("AKARIOS_VILLAGE_210010000"));
	}
}
