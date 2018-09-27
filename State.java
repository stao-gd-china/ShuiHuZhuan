package gof.behavioral.state.cleancode;

import lombok.Getter;

/*
https://www.safaribooksonline.com/videos/design-patterns-clean/9780135485965/9780135485965-DPCC_E28?autoplay=false

# Turnstile State transition table

Event: public (outside)
Action: private (internal)

| Given State | Event (outside) | Next State | Action (internal) |
|-------------|-----------------|------------|-------------------|
| Locked      | Coin            | Unlocked   | Unlock, Alarm-off |
| Locked      | Pass            | Locked     | Alarm             |
| Unlocked    | Coin            | Unlocked   | Thank you         |
| Unlocked    | Pass            | Locked     | Lock              |

Given-When-Then convention

Given that we are in the Locked state when we get a coin event, then we go to the Unlocked state and invoke the Unlock action
Given that we are in the Unlocked state when we get a Pass event, then we go back to the Locked state and invoke the Lock action

# SMC Parser

http://smc.sourceforge.net/SmcManual.htm

# Backus-Naur form

https://en.wikipedia.org/wiki/Backus%E2%80%93Naur_form
https://www.youtube.com/watch?v=8cEhCx8pwio
 */

// What
interface TurnstileState {
  void pass(TurnstileFSM fsm);

  void coin(TurnstileFSM fsm);
}

enum OneCoinTurnstileState implements TurnstileState {
  LOCKED {
    @Override
    public void pass(TurnstileFSM fsm) {
      fsm.alarm();
    }

    @Override
    public void coin(TurnstileFSM fsm) {
      fsm.setState(UNLOCKED);
      fsm.unlock();
    }
  },

  UNLOCKED {
    @Override
    public void pass(TurnstileFSM fsm) {
      fsm.setState(LOCKED);
      fsm.lock();
    }

    @Override
    public void coin(TurnstileFSM fsm) {
      fsm.thankyou();
    }
  }
}

// How
abstract class TurnstileFSM {
  private TurnstileState state;

  public void pass() {
    state.pass(this);
  }

  public void coin() {
    state.coin(this);
  }

  public void setState(TurnstileState state) {
    this.state = state;
  }

  protected abstract void alarm();

  protected abstract void lock();

  protected abstract void unlock();

  protected abstract void thankyou();
}

class TurnstileFSMImpl extends TurnstileFSM {
  private TurnstileFSM fsm;
  @Getter
  private String actions;

  public TurnstileFSMImpl() {
    fsm = this;
    fsm.setState(OneCoinTurnstileState.LOCKED);
    actions = "";
  }

  @Override
  public void alarm() {
    actions += "A";
  }

  @Override
  public void lock() {
    actions += "L";
  }

  @Override
  public void unlock() {
    actions += "U";
  }

  @Override
  public void thankyou() {
    actions += "T";
  }
}

public class State {
  public static void main(String[] args) {
    coinPassLOCKED();

    passLOCKED();
  }

  private static void passLOCKED() {
    System.out.println("# passLOCKED");
    TurnstileFSM turnstileFSM = new TurnstileFSMImpl();
    turnstileFSM.pass();
    System.out.println(((TurnstileFSMImpl) turnstileFSM).getActions());
  }

  private static void coinPassLOCKED() {
    System.out.println("# coinPassLOCKED");
    TurnstileFSM turnstileFSM = new TurnstileFSMImpl();
    turnstileFSM.coin();
    turnstileFSM.pass();
    System.out.println(((TurnstileFSMImpl) turnstileFSM).getActions());
  }
}
/*
# coinPassLOCKED
UL
# passLOCKED
A
 */
