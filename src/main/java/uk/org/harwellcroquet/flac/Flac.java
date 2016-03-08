package uk.org.harwellcroquet.flac;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

public class Flac {

	public static void main(String[] args) throws CannotReadException, IOException, ReadOnlyFileException,
			InvalidAudioFrameException, TagException {

		Logger.getLogger("org.jaudiotagger").setLevel(Level.OFF);
		Path dir = new File("E:/FLAC/Composer/").toPath();

		Set<String> codes = new HashSet<>();
		GetRipResult getRipResult = new GetRipResult(codes);
		Files.walkFileTree(dir, getRipResult);

		for (String code : codes) {
			System.out.println(code);
		}
	}

	public static class GetRipResult extends SimpleFileVisitor<Path> {

		private Set<String> codes;

		public GetRipResult(Set<String> codes) {
			this.codes = codes;
		}

		@Override
		public FileVisitResult visitFile(Path path, BasicFileAttributes attr) throws IOException {

			if (path.toString().toLowerCase().endsWith(".flac")) {
				try {
					Tag tag = AudioFileIO.read(path.toFile()).getTag();
					String rip = tag.getFirst("ACCURATERIPRESULT");
					int n = rip.indexOf("[");

					rip = rip.substring(12, n).trim();
					if (!rip.startsWith("Accurate")) {
						codes.add(rip);
						System.out.println(path + " " + rip);
					}
					
				} catch (Exception e) {
					System.out.println("Bad file found " + e.getClass().getSimpleName() + " " + e.getMessage() + " "
							+ path);
				}
			}

			return FileVisitResult.CONTINUE;
		}

	}

}
