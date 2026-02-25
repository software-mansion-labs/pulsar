import Pulsar from './NativeRNPulsar';
import type { Pattern, PatternComposer } from './types';

// workaround for RN prototype caching issue 
Pulsar.PatternComposer_play;

export default class ImperativePatternComposer implements PatternComposer {
  private patternId = -1;

  play() {
    'worklet';
    if (this.patternId !== -1) {
      Pulsar.PatternComposer_play(this.patternId);
    }
  }

  parse(pattern: Pattern) {
    const patternId = Pulsar.PatternComposer_parsePattern(pattern);
    this.patternId = patternId;
  };

  isParsed() {
    return this.patternId !== -1;
  }

  release() {
    Pulsar.PatternComposer_release(this.patternId);
    this.patternId = -1;
  };
}

