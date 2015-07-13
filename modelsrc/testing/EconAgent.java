package testing;

import java.util.ArrayList;

public class EconAgent {	

	public EconAgent() {
		
	}
	
	public EconAgent(IAgentTrait... iTraits) {
		int i;
		traits = new ArrayList<IAgentTrait>(iTraits.length);
		for(i=0; i<iTraits.length; ++i) {
			addTrait(iTraits[i]);
		}
	}

	public void addTrait(IAgentTrait trait) {
		traits.add(trait);		
	}
	public void removeTrait(IAgentTrait trait) {
		traits.remove(trait);		
	}
	
	ArrayList<IAgentTrait> traits;
	
//	public boolean receive(Message message) {
//		for(Message.IReceiver module : this) {
//			if(module.receive(message)) {
//				return(true);
//			}
//		}
//		return(false);
//	}

//	public boolean terminate(Contract contract) {
//		for(Contract.Set module : this) {
//			if(Contract.IIssuer.class.isAssignableFrom(module.getClass())) {
//				if(((Contract.IIssuer)module).terminate(contract)) {
//					return(true);
//				}
//			}
//		}
//		return(false);
//	}
		
	/***
	public <T> T getInterface(Class<T> clazz) {
		for(Contract.Set module : this) {
			if(clazz.isAssignableFrom(module.getClass())) {
				return((T)module);
			}
		}
		return(null);
	}
	***/
	
	/***
	 * @return an iterator that iterates over all contracts that belong to
	 * type T
	 */
//	public <T> Iterator<T> iteratorOf(Class<T> runtimeType) {
//		return(new TypeFilteredIterator<T>(runtimeType));
//	}

	/***
	 * @return An iterable container that contains all the contracts that
	 * belong to type T
	 */
//	public <T> Iterable<T> setOf(Class<T> elementType) {
//		return(new FlattenedIterable<T>(new IterableOfType<Iterable<T> >(Iterable.class, this)));
//	}

//	public class TypeFilteredIterable<T> implements Iterable<T> {
//		public TypeFilteredIterable(Class<T> clazz) {
//			elementClazz = clazz;
//		}
//		public Iterator<T> iterator() {
//			return(new TypeFilteredIterator<T>(elementClazz));
//		}
//		Class<T> elementClazz;
//	}
	/***
	public class TypeFilteredIterator<T> implements Iterator<T> {
		public TypeFilteredIterator(Class<T> iclazz) {
			classFilter = iclazz;
			moduleIterator = EconAgent.this.iterator();
			nextModule();
		}
		@Override
		public boolean hasNext() {
			return(moduleIterator.hasNext() || contractIterator.hasNext());
		}

		@Override
		public T next() {
			if(!contractIterator.hasNext()) {
				nextModule();
			}
			return(contractIterator.next());
		}

		@Override
		public void remove() {
			contractIterator.remove();
		}

//		@Override
//		public void forEachRemaining(Consumer<? super T> action) {
//			while(hasNext()) action.accept(next());
//		}
		
		@SuppressWarnings("unchecked")
		private void nextModule() {
			do {
				currentModule = moduleIterator.next();
			} while(currentModule != null && 
					(!classFilter.isAssignableFrom(currentModule.getElementClass()) ||
					 currentModule.iterator().hasNext() == false));
			if(currentModule != null) {
				contractIterator = (Iterator<T>)currentModule.iterator();
			} else {
				contractIterator = new ArrayList<T>().iterator();
			}
		}
		
		Class<T> classFilter;
		Iterator<T> contractIterator;
		Iterator<Contract.Set> moduleIterator;
		Contract.Set currentModule;
	}
	***/
}
