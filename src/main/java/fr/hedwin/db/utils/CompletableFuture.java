/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: CompletableFuture
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.utils;

import fr.hedwin.utils.fonctional.ThrowableConsumer;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class CompletableFuture<V> implements Future<V> {

    private Consumer<Exception> error;
    private Runnable thenFinally;
    private Callable<V> v;

    public static <V> CompletableFuture<V> async(Callable<V> v){
        return new CompletableFuture<>(v);
    }

    public CompletableFuture(Callable<V> v){
        this.v = v;
    }

    @Override
    public Future<V> then(ThrowableConsumer<V> consumer) {
        new Thread(() -> {
            try {
                consumer.accept(call());
            } catch (Exception e) {
                if(error != null) error.accept(e);
                //else System.err.println(e.getMessage());
            } finally {
                if(thenFinally != null) thenFinally.run();
            }
        }).start();
        return this;
    }

    @Override
    public Future<V> error(Consumer<Exception> error) {
        this.error = error;
        return this;
    }

    @Override
    public void thenFinally(Runnable thenFinally) {
        this.thenFinally = thenFinally;
    }

    @Override
    public V call() throws Exception {
        return v.call();
    }

}
