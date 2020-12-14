package com.piratecatai.game;

import com.badlogic.gdx.ai.fsm.State;
import com.badlogic.gdx.ai.msg.Telegram;

public enum NPCshipState implements State<NPCship> {

    GLOBAL_STATE() {
        @Override
        public void enter(NPCship ship) {

        }

        @Override
        public void update(NPCship ship) {
            ship.steering = ship.steeringBehavior.calculateSteering(ship.steering);
            ship.body.setLinearVelocity(ship.steering.linear);
            ship.turnAndSetPitch();
            ship.coolDownManager.update();
        }

        @Override
        public void exit(NPCship ship) {

        }

    },

    ENGAGE_PLAYER() {
        @Override
        public void enter(NPCship ship) {
            ship.steeringBehavior = ship.matchVelocity;
        }

        @Override
        public void update(NPCship ship) {
            if (ship.distanceToPlayer > 120) ship.stateMachine.changeState(PURSUE_PLAYER);

            if (ship.checkIfPlayerInShootingRange(ship.LEFT) && ship.coolDownManager.done("shootingCD")) {
                ship.shoot(ship.LEFT, 300f, 4f);
                System.out.println("LEFT");
                ship.coolDownManager.put("shootingCD", 1f);
            }
            if (ship.checkIfPlayerInShootingRange(ship.RIGHT) && ship.coolDownManager.done("shootingCD")) {
                ship.shoot(ship.RIGHT, 300f, 4f);
                System.out.println("RIGHT");
                ship.coolDownManager.put("shootingCD", 1f);
            }
        }

        @Override
        public void exit(NPCship ship) {

        }
    },

    PURSUE_PLAYER() {
        @Override
        public void enter(NPCship ship) {
            ship.steeringBehavior = ship.prioritySteeringPursue;
        }

        @Override
        public void update(NPCship ship) {
            if (ship.distanceToPlayer <= 90) ship.stateMachine.changeState(ENGAGE_PLAYER);
            ship.generateNewPathToPlayer();
            ship.followPath.setPath(ship.pathToPlayer);
        }

        @Override
        public void exit(NPCship ship) {

        }

    };

    @Override
    public boolean onMessage(NPCship entity, Telegram telegram) {
        return false;
    }
}
