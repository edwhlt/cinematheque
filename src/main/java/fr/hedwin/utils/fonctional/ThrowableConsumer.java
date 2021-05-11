/*::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
 Copyright (c) 2021.
 Project: Cinémathèque
 Author: Edwin HELET & Julien GUY
 Class: ThrowableConsumer
 :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::*/

package fr.hedwin.utils.fonctional;

@FunctionalInterface
public interface ThrowableConsumer<V> {
    void accept(V v) throws Exception;
}
