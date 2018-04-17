import java.util.*;

class Player {

    private static final String WAIT = "WAIT";
    private static final int MAX_PLAYERS = 3;
    private static final int REAPER = 0;
    private static final int DESTROYER = 1;
    private static final int TANKER = 3;
    private static final int WRECK = 4;
    private static final int RADIUS = 6000;

    private Scanner in;
    private List<Bot> bots;
    private List<Unit> reapers;
    private List<Unit> destroyers;
    private List<Unit> tankers;
    private List<Unit> wrecks;

    public Player() {
        in = new Scanner(System.in);
        bots = new ArrayList<>();
        for (int i = 0; i < MAX_PLAYERS; i++) {
            bots.add(new Bot());
        }
        reapers = new ArrayList<>();
        destroyers = new ArrayList<>();
        tankers = new ArrayList<>();
        wrecks = new ArrayList<>();
    }

    public void gameLoop() {
        while (true) {
            clearDataStructures();
            inputForARound();

            Output output = selectMove();

            for (String line : output.line) {
                System.out.println(line);
            }
        }
    }

    private Unit closestUnit(Unit from, List<Unit> to) {
        if (to.isEmpty()) {
            return null;
        }

        double minDistance = 2 * RADIUS;
        int minUnit = 0;
        for (int i = 0; i < to.size(); i++) {
            double dist = from.distance(to.get(i));
            if (dist < minDistance) {
                minDistance = dist;
                minUnit = i;
            }
        }
        return to.get(minUnit);
    }

    private Output selectMove() {
        Output output = new Output();

        Unit closestTanker = null;
        if (!destroyers.isEmpty()) {
            closestTanker = closestUnit(destroyers.get(0), tankers);
        }

        Unit closestWreck = closestUnit(reapers.get(0), wrecks);

        if (closestTanker != null) {
            output.line[1] = closestTanker.x + " " + closestTanker.y + " 150";
        }
        if (closestWreck != null) {
            output.line[0] = closestWreck.x + " " + closestWreck.y + " 200";
        }


        return output;
    }

    private void clearDataStructures() {
        reapers.clear();
        destroyers.clear();
        wrecks.clear();
        tankers.clear();
    }

    private void inputForARound() {
        for (int i = 0; i < MAX_PLAYERS; i++) {
            bots.get(i).score = in.nextInt();
        }
        for (int i = 0; i < MAX_PLAYERS; i++) {
            bots.get(i).rage = in.nextInt();
        }


        int unitCount = in.nextInt();
        for (int i = 0; i < unitCount; i++) {
            int unitId = in.nextInt();
            int unitType = in.nextInt();
            int player = in.nextInt();
            float mass = in.nextFloat();
            int radius = in.nextInt();
            int x = in.nextInt();
            int y = in.nextInt();
            int vx = in.nextInt();
            int vy = in.nextInt();
            int extra = in.nextInt();
            int extra2 = in.nextInt();

            Unit unit = new Unit(unitId, unitType,
                    player, mass, radius,
                    x, y, vx, vy,
                    extra, extra2);

            if (unitType == REAPER) {
                reapers.add(unit);
            } else if (unitType == WRECK) {
                wrecks.add(unit);
            } else if (unitType == DESTROYER) {
                destroyers.add(unit);
            } else if (unitType == TANKER) {
                tankers.add(unit);
            }
        }
    }


    public static void main(String args[]) {
        Player player = new Player();
        player.gameLoop();
    }

    class Unit {

        public int id;
        public int type;
        public int player;
        public float mass;
        public int radius;
        public int x;
        public int y;
        public int vx;
        public int vy;
        public int extra;
        public int extra2;

        public Unit(int id, int type, int player, float mass, int radius, int x, int y, int vx, int vy, int extra, int extra2) {
            this.id = id;
            this.type = type;
            this.player = player;
            this.mass = mass;
            this.radius = radius;
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.extra = extra;
            this.extra2 = extra2;
        }

        private double distance(Unit unit) {
            return Math.sqrt((this.x - unit.x) * (this.x - unit.x) + (this.y - unit.y) * (this.y - unit.y));
        }
    }

    class Bot {

        public int score;
        public int rage;

    }

    class Output {

        public String[] line = new String[]{WAIT, WAIT, WAIT};
    }
}