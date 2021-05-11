/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: Future
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.db.utils;

import fr.hedwin.utils.fonctional.ThrowableConsumer;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

public interface Future<V> extends Callable<V> {

    Future<V> then(ThrowableConsumer<V> consumer);
    Future<V> error(Consumer<Exception> error);
    void thenFinally(Runnable runnable);

}
