import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;

public class AutomatonImpl implements Automaton {

    class StateLabelPair {
        int state;
        char label;
        public StateLabelPair(int state_, char label_) { state = state_; label = label_; }

        @Override
        public int hashCode() {
            return Objects.hash((Integer) state, (Character) label);
        }

        @Override
        public boolean equals(Object o) {
            StateLabelPair o1 = (StateLabelPair) o;
            return (state == o1.state) && (label == o1.label);
        }
    }

    HashSet<Integer> start_states;
    HashSet<Integer> accept_states;
    HashSet<Integer> current_states;
    HashMap<StateLabelPair, HashSet<Integer>> transitions ;

    public AutomatonImpl() {
        start_states = new HashSet<Integer>();
        accept_states = new HashSet<Integer>();
        transitions = new HashMap<StateLabelPair, HashSet<Integer>>();
    }

    @Override
    public void addState(int s, boolean is_start, boolean is_accept) {
        for (int state : new int[] {s}) {  // Replace {s} with an array of states if you have multiple
        if (is_start) {
            start_states.add(state);
        }
        if (is_accept) {
            accept_states.add(state);
        }
    }
}

    @Override
    public void addTransition(int s_initial, char label, int s_final) {
        StateLabelPair key = new StateLabelPair(s_initial, label);
        transitions.putIfAbsent(key, new HashSet<>());
        transitions.get(key).add(s_final);
    }

    @Override
    public void reset() {
        current_states = new HashSet<>(start_states);
    }

    @Override
    public void apply(char input) {
        HashSet<Integer> next_states = new HashSet<>();
        current_states.forEach(state -> {
            StateLabelPair key = new StateLabelPair(state, input);
                if (transitions.containsKey(key)) {
                next_states.addAll(transitions.get(key));
                }
         });
        current_states = next_states;
    }

    @Override
    public boolean accepts() {
    final boolean[] result = {false};  
    current_states.forEach(state -> {
        if (accept_states.contains(state)) {
            result[0] = true;
        }
    });

    return result[0];
}

    @Override
    public boolean hasTransitions(char label) {
        final boolean[] result = {false};  
        current_states.forEach(state -> {
        StateLabelPair key = new StateLabelPair(state, label);
            if (transitions.containsKey(key)) {
            result[0] = true;
            }
        });

    return result[0];
    }

}
