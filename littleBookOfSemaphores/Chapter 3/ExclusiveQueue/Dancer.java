public interface Dancer {
    /**
     * A default method to simulate the act of dancing for a period of time.
     * 
     * @param role The role of the dancer (e.g., "Leader" or "Follower").
     */
    default void dance(String role) {
        System.out.println("\n>>> " + role + " has started dancing.");
        try {
            // Simulate the dance taking some time.
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("\n<<< " + role + " has finished dancing.");
    }
}