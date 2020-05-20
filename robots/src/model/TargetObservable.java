package model;

public interface TargetObservable {
	 	public void registerObserver(TargetObserver obs);

	    public void unregisterObserver(TargetObserver obs);

	    public void notifyObservers();
}
