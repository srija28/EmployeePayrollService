package com.capgemini;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.WatchKey;
import java.nio.file.WatchEvent.Kind;
import java.util.HashMap;
import java.util.Map;

public class Java8WatchService {
	private static final Kind<?> ENTRY_CREATE = null;
	private static final Kind<?> ENTRY_MODIFY = null;
	private static final Kind<?> ENTRY_DELETE = null;
	private final WatchService watcher;
	private final Map<WatchKey, Path> dirWatchers;
	
	//Creates a watchservice and registers the given directory 
	Java8WatchService(Path dir) throws IOException{
		this.watcher = FileSystems.getDefault().newWatchService();
		this.dirWatchers = new HashMap<WatchKey, Path>();
		scanAndRegisterDirectories(dir);
	}
	
	// Register the given directory with watch service 
	
	private void registerDirWatchers(Path dir) throws IOException{
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
		dirWatchers.put(key, dir);
	}
	
	//register the given directory and all its sub directory with WatcherService
	
	private void scanAndRegisterDirectories(final Path start) throws IOException{
		Files.walkFileTree(start, new SimpleFileVisitor<Path>(){
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				registerDirWatchers(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}
	// process all events for the keys queued to the watchers
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	void processEvents() {
		while(true) {
			WatchKey key;
			try {
				key =watcher.take();
			}
			catch(InterruptedException x) {
				return;
			}
			Path dir = dirWatchers.get(key);
			if(dir==null) continue;
			for(WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				Path name = ((WatchEvent<Path>) event).context();
				Path child = dir.resolve(name);
				System.out.format("%s : %s\n", event.kind().name(), child);
				
				// if directory is created then register it and its sub directories
				if(kind == ENTRY_CREATE) {
					try {
						if(Files.isDirectory(child)) {
							scanAndRegisterDirectories(child);
						}
					}catch(IOException x) {
					}
				}
				else if(kind.equals(ENTRY_DELETE)) {
					if(Files.isDirectory(child)) dirWatchers.remove(key);
				}
			}
			boolean valid = key.reset();
			if(!valid) {
				dirWatchers.remove(key);
				if(dirWatchers.isEmpty()) break;
			}
					
		}
	}
}
