// Minimal stream polyfill
export class Readable {
    on() { return this; }
    once() { return this; }
    emit() { return this; }
    pipe() { return this; }
    end() { return this; }
    destroy() { return this; }
}

export class Writable {
    on() { return this; }
    once() { return this; }
    emit() { return this; }
    write() { return this; }
    end() { return this; }
}

export default {
    Readable,
    Writable,
    Stream: Readable
};
