package sample.stream;

import java.util.Arrays;
import scala.runtime.BoxedUnit;
import akka.actor.ActorSystem;
import akka.dispatch.OnComplete;
import akka.stream.ActorFlowMaterializer;
import akka.stream.javadsl.Source;

public class BasicTransformation {
  public static void main(String[] args) {
    final ActorSystem system = ActorSystem.create("Sys");
    final ActorFlowMaterializer materializer = ActorFlowMaterializer.create(system);

    final String text =
      "Lorem Ipsum is simply dummy text of the printing and typesetting industry. " +
      "Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, " +
      "when an unknown printer took a galley of type and scrambled it to make a type " +
      "specimen book.";

    Source.from(Arrays.asList(text.split("\\s"))).
      // transform
      map(line -> line.toUpperCase()).
      // print to console
      runForeach(System.out::println, materializer).
      onComplete(new OnComplete<BoxedUnit>() {
        @Override public void onComplete(Throwable failure, BoxedUnit success) throws Exception {
          system.shutdown();
        }
      }, system.dispatcher());
  }

}