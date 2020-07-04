import { GroupByPipe } from './group-by.pipe';

describe('GroupBypipePipe', () => {
  it('create an instance', () => {
    const pipe = new GroupByPipe();
    expect(pipe).toBeTruthy();
  });
});
